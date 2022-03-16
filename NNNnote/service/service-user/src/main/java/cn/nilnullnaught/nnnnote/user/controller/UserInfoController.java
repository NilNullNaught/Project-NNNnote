package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

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
        String ID = JwtUtils.getIdByJwtToken(request);
        UserInfo userInfo = userInfoService.getById(ID);
        return R.ok().data("data", userInfo);
    }

    @ApiOperation("根据 id 获取用户信息")
    @GetMapping("/getUserInfoById/{Id}")
    public R getUserInfoById(@PathVariable String Id) {
        UserInfo userInfo = userInfoService.getById(Id);
        return R.ok().data("data", userInfo);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/alterUserInfo")
    public R alterUserInfo(@RequestHeader("token") String token,@RequestBody UserInfo userInfo) {
        String id = JwtUtils.getIdByJwtToken(token);
        userInfo.setId(id);
        userInfoService.updateUserInfo(userInfo);
        return R.ok();
    }

    @ApiOperation("根据 id 列表获取用户头像和昵称信息")
    @PostMapping("/getUserAvatarAndNickNameByIdList")
    public R getUserAvatarAndNickNameByIdList(@RequestBody Set<String> idList){

        var qw = new QueryWrapper<UserInfo>();
        qw.select("id","nickname","avatar");
        qw.groupBy("id");
        qw.in("id",idList);
        var data = userInfoService.listMaps(qw);

        return R.ok().data("data",data);
    }
}
