package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.user.service.UserMemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户会员信息表 前端控制器
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user/user-member")
public class UserMemberController {

    @Autowired
    private UserMemberService userMemberService;

    @ApiOperation("获取用户会员信息")
    @GetMapping("getUserMember")
    public R getUserMember(@RequestHeader("token") String token) {

        String userId = JwtUtils.getIdByJwtToken(token);

        var result = userMemberService.getById(userId);

        return R.ok().data("data", result);
    }

    @ApiOperation("通过 ID 获取用户会员信息")
    @GetMapping("getUserMemberById")
    public R getUserMemberById(@RequestParam("userId") String userId) {
        var result = userMemberService.getById(userId);
        return R.ok().data("data", result);
    }

    @ApiOperation(value="修改会员信息",notes = "该接口不对用户开放")
    @PostMapping("alterUserMember")
    public R alterUserMember(@RequestParam("userId") String userId,@RequestParam("isMember") Boolean isMember) {
        userMemberService.alterUserMember(userId,isMember);
        return R.ok();
    }

}
