package cn.nilnullnaught.nnnnote.sms.controller;

import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.sms.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("sms/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "向指定邮箱发送验证码")
    @GetMapping(value = "/sendCode/{emailAddress}")
    public R sendCode(@PathVariable String emailAddress){
        emailService.sendCode(emailAddress);
        return R.ok();
    }

}
