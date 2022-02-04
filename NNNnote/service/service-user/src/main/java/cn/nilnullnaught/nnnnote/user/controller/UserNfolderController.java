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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public R getUserNfolder(HttpServletRequest request){
        String ID = JwtUtils.getIdByJwtToken(request);
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",ID);
        queryWrapper.orderByAsc("gmt_modified");
        List<UserNfolder> list=userNfolderService.list(queryWrapper);
        return R.ok().data("data",list);
    }

    @ApiOperation("根据用户 id 获取笔记文件夹列表（通过路径传递用户ID）")
    @GetMapping("/getUserNfolder/{userId}")
    public R getUserNfolderList(@PathVariable("userId") String userId){
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserNfolder> list=userNfolderService.list(queryWrapper);
        return R.ok().data("data",list);
    }

    @ApiOperation("根据用户 id 创建新的笔记文件夹，文件夹描述可以为空")
    @PostMapping("/addUserNfolder")
    public R addUserNfolder(HttpServletRequest request,
                            @RequestParam("nfolderName") String nfolderName,
                            @RequestParam("description") String description){
        String ID = JwtUtils.getIdByJwtToken(request);

        //判断文件夹名是否已存在
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",ID);
        queryWrapper.eq("folder_name",nfolderName);
        if (!userNfolderService.list(queryWrapper).isEmpty()){
            throw new MyCustomException(20001,"文件夹名不可重复");
        }


        UserNfolder userNfolder = new UserNfolder();
        userNfolder.setUserId(ID);
        userNfolder.setFolderName(nfolderName);
        userNfolder.setFolderDescription(description);

        userNfolderService.save(userNfolder);
        return R.ok();
    }

    @ApiOperation("根据文件夹 ID 修改笔记文件夹信息")
    @PostMapping("/alterUserNfolder/{nfolderID}/{folderName}/{description}/")
    public R alterUserNfolder(@PathVariable String nfolderID,
                              @PathVariable String folderName,
                              @PathVariable String description){
        UpdateWrapper<UserNfolder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",nfolderID);
        updateWrapper.set("folder_name",folderName);
        updateWrapper.set("folder_description",description);

        userNfolderService.update(updateWrapper);
        return R.ok();
    }
}
