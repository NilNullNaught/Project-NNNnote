package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.client.sms.SmsEmailClient;
import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserCheck;
import cn.nilnullnaught.nnnnote.entity.user.UserMessage;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import cn.nilnullnaught.nnnnote.user.vo.LoginVo;
import cn.nilnullnaught.nnnnote.user.vo.RegisterVo;
import cn.nilnullnaught.nnnnote.user.vo.ResetPasswordVo;
import cn.nilnullnaught.nnnnote.user.vo.SettingSafeVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public R login(@RequestBody LoginVo LoginVo){
        String token = userCheckService.userLogin(LoginVo);
        return R.ok().data("token",token);
    }

    @ApiOperation("用户密码重置")
    @PostMapping("/resetPassword")
    public R restPassword(@RequestBody ResetPasswordVo resetPasswordVo){
        userCheckService.restPassword(resetPasswordVo);
        return R.ok();
    }

    @ApiOperation("获取用户安全设置相关数据")
    @GetMapping("/alterUserSafeInfo")
    public R alterUserSafeInfo(HttpServletRequest request){
        String ID = JwtUtils.getIdByJwtToken(request);
        UserCheck userCheck = userCheckService.getById(ID);
        SettingSafeVo settingSafeVo = new SettingSafeVo();
        BeanUtils.copyProperties(userCheck,settingSafeVo);
        return  R.ok().data("data",settingSafeVo);
    }

    @ApiOperation("用户邮箱绑定与解除绑定")
    @PostMapping("/alterUserEmail")
    public R alterUserEmail(
            HttpServletRequest request,
            @RequestParam(name = "email")String email,
            @RequestParam(name = "code")String code){
        String ID = JwtUtils.getIdByJwtToken(request);
        userCheckService.alterUserEmail(ID,email,code);
        return R.ok();
    }

}
