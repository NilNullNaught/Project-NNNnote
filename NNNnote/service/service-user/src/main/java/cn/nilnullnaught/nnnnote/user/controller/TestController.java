package cn.nilnullnaught.nnnnote.user.controller;

import cn.nilnullnaught.nnnnote.client.sms.SmsEmailClient;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserMessage;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyGlobalExceptionHandler;
import cn.nilnullnaught.nnnnote.user.mapper.UserCheckMapper;
import cn.nilnullnaught.nnnnote.user.service.UserCheckService;
import cn.nilnullnaught.nnnnote.user.service.UserMessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/test")
public class TestController {

    @Autowired
    private UserMessageService userMessageService;


    @Autowired
    private UserCheckMapper userCheckMapper;

    @Autowired
    private SmsEmailClient smsEmailClient;

    @ApiOperation("测试异常处理")
    @GetMapping("/exception")
    public R exception()  {

        if (true){
            throw new MyCustomException(20001,"执行 MyCustomException 处理");
        }
        return R.ok();
    }

    @ApiOperation("测试Apiclient")
    @GetMapping("/sendEmail")
    public R sendEmail()  {
        return smsEmailClient.sendCode("2961177593@qq.com");
    }

    @ApiOperation("生成一条 UserMessage 数据")
    @GetMapping("/generate")
    public R generate()  {
        UserMessage userMessage = new UserMessage();
        userMessageService.save(userMessage);
        return R.ok();
    }

    @ApiOperation("Mybatis 测试")
    @GetMapping("/count")
    public R count()  {
        return R.ok().message(userCheckMapper.countUser().toString());
    }
}
