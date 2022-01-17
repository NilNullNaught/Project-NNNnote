package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.client.sms.SmsEmailClient;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserCheck;
import cn.nilnullnaught.nnnnote.entity.user.UserMessage;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import cn.nilnullnaught.nnnnote.user.vo.RegisterVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户登录验证表 前端控制器
 * 同时提供登录与注册功能
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user/user-check")
public class UserCheckController {


    @Autowired
    private UserCheckService userCheckService;

    @Autowired
    private SmsEmailClient smsEmailClient;

    @ApiOperation("验证手机号是否已被注册")
    @GetMapping("/checkMobile/{mobile}")
    public R checkPhone(@PathVariable("mobile") String mobile){
        Boolean result = userCheckService.checkMobile(mobile);
        return R.ok().data("result",result);
    }

    @ApiOperation("验证邮箱是否已被注册")
    @GetMapping("/checkEmail/{email}")
    public R checkEmail(@PathVariable("email") String email){
        Boolean result = userCheckService.checkEmail(email);
        return R.ok().data("result",result);
    }

    @ApiOperation("调用 sms 服务，发送验证码")
    @GetMapping("/sendCode/{email}")
    public R sendCode(@PathVariable("email") String email){
        return smsEmailClient.sendCode(email);
    }


    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo){
        userCheckService.userRegister(registerVo);
        return R.ok();
    }

    @ApiOperation("用户登录")
    @PostMapping("login")
    public R login(@RequestBody UserCheck userCheck){
        String token = userCheckService.userLogin(userCheck);
        return R.ok().data("token",token);
    }
}
