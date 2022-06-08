package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserDynamic;
import cn.nilnullnaught.nnnnote.entity.user.UserFollow;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.service.UserFollowService;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * <p>
 * 用户关注表 前端控制器
 * </p>
 *
 * @author NilNullNaught
 * @since 2022-06-05
 */
@RestController
@RequestMapping("/user/user-follow")
public class UserFollowController {
    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("关注与取消关注")
    @PostMapping("followAndCancel")
    public R followAndCancel(@RequestHeader("token") String token, @RequestParam("followUserID") String followUserID) {
        var userId = JwtUtils.getIdByJwtToken(token);

        userFollowService.followAndCancel(userId, followUserID);

        return R.ok();
    }

    @ApiOperation("查询是否关注该用户")
    @GetMapping("isFollowed")
    public R isFollowed(@RequestHeader("token") String token, @RequestParam("followUserID") String followUserID){

        var userId = JwtUtils.getIdByJwtToken(token);

        if (userId == followUserID) throw new MyCustomException(20001,"不能关注自己");

        var qw = new QueryWrapper<UserFollow>();
        qw.eq("user_id",userId);
        qw.eq("follow_userId",followUserID);

       var result = userFollowService.getOne(qw);
       if (result == null){
           return R.ok().data("data",false);
       }else {
           return R.ok().data("data",true);
       }

    }

    @ApiOperation("查询关注列表")
    @GetMapping("getFollowList")
    public R getFollowList(@RequestHeader("token") String token) {
        var userId = JwtUtils.getIdByJwtToken(token);

        var qw = new QueryWrapper<UserFollow>();
        qw.eq("user_id",userId);
        var userFollowList = userFollowService.list(qw);

        var followUserIdList = userFollowList.stream().map(UserFollow::getFollowUserid).collect(Collectors.toList());

        if (!followUserIdList.isEmpty()){
            var qw2 = new QueryWrapper<UserInfo>();
            qw2.in("id",followUserIdList);
            var result = userInfoService.list(qw2);


            return R.ok().data("data",result);

        }else {
            return R.ok().data("data",null);
        }


    }
}
