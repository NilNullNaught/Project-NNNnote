package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.service.UserNfolderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户文件夹表 前端控制器
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user/user-nfolder")
public class UserNfolderController {

    @Autowired
    private UserNfolderService userNfolderService;


    @ApiOperation("根据用户 id 获取笔记文件夹列表")
    @GetMapping("/getUserNfolder")
    public R getUserNfolder(HttpServletRequest request) {
        String ID = JwtUtils.getIdByJwtToken(request);
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", ID);
        queryWrapper.orderByAsc("gmt_modified");
        List<UserNfolder> list = userNfolderService.list(queryWrapper);
        return R.ok().data("data", list);
    }

    @ApiOperation("根据用户 id 获取笔记文件夹列表（通过路径传递用户ID）")
    @GetMapping("/getUserNfolder/{userId}")
    public R getUserNfolderList(@PathVariable("userId") String userId) {
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<UserNfolder> list = userNfolderService.list(queryWrapper);
        return R.ok().data("data", list);
    }

    @ApiOperation("根据用户 id 创建新的笔记文件夹，文件夹描述可以为空")
    @PostMapping("/addUserNfolder")
    public R addUserNfolder(HttpServletRequest request,
                            @RequestParam("nfolderName") String nfolderName,
                            @RequestParam("description") String description) {
        String ID = JwtUtils.getIdByJwtToken(request);

        //判断文件夹名是否已存在
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", ID);
        queryWrapper.eq("folder_name", nfolderName);
        if (!userNfolderService.list(queryWrapper).isEmpty()) {
            throw new MyCustomException(20001, "文件夹名不可重复");
        }


        UserNfolder userNfolder = new UserNfolder();
        userNfolder.setUserId(ID);
        userNfolder.setFolderName(nfolderName);
        userNfolder.setFolderDescription(description);

        userNfolderService.save(userNfolder);
        return R.ok();
    }

    @ApiOperation("根据文件夹 ID 修改笔记文件夹信息")
    @PostMapping("/alterUserNfolder")
    public R alterUserNfolder(@RequestParam("nfolderID") String nfolderID,
                              @RequestParam("folderName") String folderName,
                              @RequestParam("description") String description) {
        UpdateWrapper<UserNfolder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", nfolderID);
        updateWrapper.set("folder_name", folderName);
        updateWrapper.set("folder_description", description);

        userNfolderService.update(updateWrapper);
        return R.ok();
    }

    @ApiOperation("根据文件夹 ID 修改笔记文件夹信息")
    @GetMapping("/getNotefolderBynFolderId/{noteFolderID}")
    public R getNotefolderByFolderId(@PathVariable String noteFolderID) {
        UserNfolder userNfolder = userNfolderService.getById(noteFolderID);
        return R.ok().data("data", userNfolder);
    }

    @ApiOperation(value = "根据用户 id 获取笔记文件夹列表并分页", notes = "分页查询文件夹列表，返回数据封装为 mapper")
    @GetMapping("/getUserNfolder/{page}/{limit}")
    public R getUserNfolder(@RequestHeader("token") String token,
                            @PathVariable long page,
                            @PathVariable long limit) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map = userNfolderService.getUserNfolderPage(userId, page, limit);
        return R.ok().data(map);
    }

    @ApiOperation(value = "删除用户文件夹", notes = "删除文件夹，可以进行批量删除。")
    @Transactional
    @DeleteMapping("/deleteUserNFolder")
    public R deleteUserNFolder(@RequestHeader("token") String token, @RequestBody List<String> nFolderList) {
        String userId = JwtUtils.getIdByJwtToken(token);

        // <- 查询需要被删除的用户文件夹
        QueryWrapper<UserNfolder> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);// 过滤文件夹，防止删除其他用户的文件夹
        qw.in("id", nFolderList);
        List<UserNfolder> userNfolderList = userNfolderService.list(qw);
        // ->

        List<String> _nFolderList = new ArrayList<>();
        for (UserNfolder userNfolder : userNfolderList) {
            //判断文件夹是否已经清空
            if (userNfolder.getNoteCount() != 0) {
                throw new MyCustomException(20001, "文件夹未清空");
            }
            _nFolderList.add(userNfolder.getId());
        }

        // <- 删除文件夹 ->
        userNfolderService.removeByIds(_nFolderList);
        return R.ok();
    }

    @ApiOperation(value = "修改用户文件夹的 note_count ", notes = "通过传入的键值对修改多个文件夹的 note_count 字段")
    @PostMapping("/alterUserNfolderNoteCount")
    public R alterUserNfolderNoteCount(@RequestBody Map<String, Long> map) {

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            UpdateWrapper<UserNfolder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", entry.getKey());
            updateWrapper.set("note_count", entry.getValue());
            userNfolderService.update(updateWrapper);
        }

        return R.ok();
    }

    @ApiOperation(value = "分页条件查询文件夹和笔记")
    @GetMapping("/getNfolderANDNote/{page}/{limit}/{condition}")
    public R getNfolderANDNote(@RequestHeader("token") String token,
                               @PathVariable long page,
                               @PathVariable long limit,
                               @PathVariable String condition) {
        String userId = JwtUtils.getIdByJwtToken(token);
        Map<String, Object> map = userNfolderService.getNfolderANDNote(userId, page, limit, condition);
        return R.ok().data(map);
    }

    @ApiOperation(value = "根据文件夹 Id 获取笔记文件夹标题")
    @PostMapping("/getNoteFolderNameByFolderId")
    public R getNoteFolderNameByFolderId(@RequestHeader("token") String token,
                                         @RequestBody List<String> idList) {
        String userId = JwtUtils.getIdByJwtToken(token);

        if (idList.size() == 0) {
            return R.ok().data("data", new HashMap<>());
        }

        // 构建查询条件，查询与 ID 对应的笔记文件夹标题
        QueryWrapper<UserNfolder> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        qw.in("id", idList);
        qw.select("id", "folder_name");
        List<UserNfolder> resultList = userNfolderService.list(qw);

        Map<String, String> resultMap = new HashMap<>();

        // 重新封装结果集，将查询条件转换为键值对（id为键，文件夹标题为值）
        for (UserNfolder userNfolder : resultList) {
            resultMap.put(userNfolder.getId(), userNfolder.getFolderName());
        }

        // 将查询结果为空（即已经被删除的笔记文件夹）的文件夹ID也封装入结果集中
        for (String id : idList) {
            if (!resultMap.containsKey(id)) {
                resultMap.put(id, null);
            }
        }

        return R.ok().data("data", resultMap);
    }
}
