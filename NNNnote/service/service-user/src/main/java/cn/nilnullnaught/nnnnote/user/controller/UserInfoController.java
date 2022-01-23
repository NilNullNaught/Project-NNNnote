package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-01-18
 */
@RestController
@RequestMapping("/user/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("根据 token 获取用户信息")
    @GetMapping("/getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        String ID = JwtUtils.getMemberIdByJwtToken(request);
        UserInfo userInfo = userInfoService.getById(ID);
        return R.ok().data("data", userInfo);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/alterUserInfo")
    public R alterUserInfo(@RequestBody UserInfo userInfo) {
        userInfoService.updateUserInfo(userInfo);
        return R.ok();
    }


}
