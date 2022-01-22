package cn.nilnullnaught.nnnnote.user.controller;

import cn.nilnullnaught.nnnnote.client.sms.SmsEmailClient;
import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.user.UserInfo;
import cn.nilnullnaught.nnnnote.exceptionhandler.MyCustomException;
import cn.nilnullnaught.nnnnote.user.mapper.UserCheckMapper;
import cn.nilnullnaught.nnnnote.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.Date;

@RestController
@RequestMapping("/user/test")
public class TestController {

    @Autowired
    private UserInfoService userInfoService;


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
        UserInfo userInfo = new UserInfo();
        userInfoService.save(userInfo);
        return R.ok();
    }

    @ApiOperation("Mybatis 测试")
    @GetMapping("/count")
    public R count()  {
        return R.ok().message(userCheckMapper.countUser().toString());
    }

    @ApiOperation("检查 token 的过期时间")
    @GetMapping("/tokenExpire")
    public R tokenExpire(HttpServletRequest request){
        //调用jwt工具类的方法。根据request对象获取token过期时间与 id，返回用户id
        Date date = JwtUtils.getExpiredTimeByJwtToken(request);
        return R.ok().data("exiration",date);
    }
}
