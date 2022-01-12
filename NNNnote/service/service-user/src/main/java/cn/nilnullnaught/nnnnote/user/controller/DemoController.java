package cn.nilnullnaught.nnnnote.user.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/demo")
@RestController
public class DemoController {

    @ApiOperation("测试")
    @GetMapping("/helloworld")
    public String helloWord(){
        return "Success";
    }
}
