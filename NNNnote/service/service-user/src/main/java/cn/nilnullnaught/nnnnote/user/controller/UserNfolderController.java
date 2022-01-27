package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserNfolder;
import cn.nilnullnaught.nnnnote.user.service.UserNfolderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("根据用户 id 获取文件夹列表")
    @GetMapping("/getUserNfolder")
    public R getUserNfolder(HttpServletRequest request){
        String ID = JwtUtils.getIdByJwtToken(request);
        QueryWrapper<UserNfolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",ID);
        queryWrapper.orderByAsc("gmt_modified");
        List<UserNfolder> list=userNfolderService.list(queryWrapper);
        return R.ok().data("data",list);
    }
}
