package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.note.NoteUserCollection;
import cn.nilnullnaught.nnnnote.entity.user.UserCfolder;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.service.UserCfolderService;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户文件夹表 前端控制器
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user/user-cfolder")
public class UserCfolderController {

    @Autowired
    private UserCfolderService userCfolderService;

    @ApiOperation(value = "获取收藏夹信息", notes = "根据收藏夹 id 获取单个收藏夹的信息")
    @GetMapping("getUserCfolderById")
    public R getUserCfolderById(@RequestHeader("token") String token, @RequestParam("cfolderId") String cfolderId) {
        String userId = JwtUtils.getIdByJwtToken(token);

        var result = userCfolderService.getById(cfolderId);

        if (userId == result.getUserId()){
            throw new MyCustomException(20001,"非法操作");
        }

        return R.ok().data("data", result);
    }

    @ApiOperation(value = "获取用户所有收藏夹", notes = "通过token获取")
    @GetMapping("getUserCfolders")
    public R getUserCfolders(@RequestHeader("token") String token) {
        String userId = JwtUtils.getIdByJwtToken(token);

        var qw = new QueryWrapper<UserCfolder>();
        qw.eq("user_id", userId);
        var data = userCfolderService.list(qw);

        return R.ok().data("data", data);
    }

    @ApiOperation(value = "分页查询笔记收藏夹列表", notes = "通过 token 获取，分页查询文件夹列表，返回数据封装为 mapper")
    @GetMapping("/getUserCfolderPaging")
    public R getUserCfolderPaging(@RequestHeader("token") String token,
                                  @RequestParam("page") long page,
                                  @RequestParam("limit") long limit) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map = userCfolderService.getUserNfolderPage(userId, page, limit);
        return R.ok().data(map);
    }


    @ApiOperation("根据用户 id 创建新的收藏夹，文件夹描述可以为空")
    @PostMapping("/addUserCfolder")
    public R addUserNfolder(@RequestHeader("token") String token,
                            @RequestParam("cfolderName") String cfolderName,
                            @RequestParam("description") String description) {
        String userId = JwtUtils.getIdByJwtToken(token);

        //判断文件夹名是否已存在
        var qw = new QueryWrapper<UserCfolder>();
        qw.eq("user_id", userId);
        qw.eq("folder_name", cfolderName);
        if (!userCfolderService.list(qw).isEmpty()) {
            throw new MyCustomException(20001, "收藏夹名不可重复");
        }

        var userCfolder = new UserCfolder();
        userCfolder.setUserId(userId);
        userCfolder.setFolderName(cfolderName);
        userCfolder.setFolderDescription(description);

        userCfolderService.save(userCfolder);
        return R.ok();
    }

    @ApiOperation("更新收藏夹笔记数")
    @PostMapping("/updateCfolderNoteCount")
    public R updateCfolderNoteCount(@RequestParam("count") Long count,
                                    @RequestParam("cfolderId") String cfolderId) {

        var uw = new UpdateWrapper<UserCfolder>();
        uw.eq("id", cfolderId);
        uw.set("note_count", count);
        userCfolderService.update(uw);
        return R.ok();
    }

    @ApiOperation(value = "删除收藏夹", notes = "根据 token 中的 Id 删除收藏夹，可以进行批量删除。")
    @Transactional
    @DeleteMapping("/deleteCFolders")
    public R deleteCFolders(@RequestHeader("token") String token, @RequestBody List<String> cFolderList) {
        String userId = JwtUtils.getIdByJwtToken(token);

        // region <- 用户校验 ->
        var qw = new QueryWrapper<UserCfolder>();
        qw.eq("user_id", userId);
        qw.in("id", cFolderList);
        var userCfolderList = userCfolderService.list(qw);
        // endregion

        // region <- 验证收藏夹是否被清空 ->
        for (var userCfolder : userCfolderList) {
            //判断文件夹是否已经清空
            if (userCfolder.getNoteCount() != 0) {
                throw new MyCustomException(20001, "文件夹未清空");
            }
        }
        // endregion

        // region <- 删除收藏夹 ->
        // 获取校验后的收藏夹列表
        var idList = userCfolderList.stream().map(item -> item.getId()).collect(Collectors.toList());
        userCfolderService.removeByIds(idList);
        // endregion

        return R.ok();
    }

    @ApiOperation(value = "修改收藏夹", notes = "根据文件夹 ID 修改收藏夹的标题与介绍信息")
    @PostMapping("/updateUserCfolder")
    public R updateUserCfolder(@RequestHeader("token") String token,
                               @RequestParam("cfolderID") String id,
                               @RequestParam("folderName") String folderName,
                               @RequestParam("description") String description) {
        String userId = JwtUtils.getIdByJwtToken(token);

        var userCfolder = new UserCfolder();
        userCfolder.setId(id);
        userCfolder.setUserId(userId);
        userCfolder.setFolderName(folderName);
        userCfolder.setFolderDescription(description);

        userCfolderService.updateById(userCfolder);
        return R.ok();
    }
}
