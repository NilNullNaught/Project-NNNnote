package cn.nilnullnaught.nnnnote.transit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("ucenter/wx")
public class TransitController {

    @GetMapping("callback")
    public String callback(String code, String state){
        String baseUrl = "redirect:http://localhost:10010/user/user-wechat/callback" +
                "?code=%s" +
                "&state=%s";
        String redirectUrl = String.format(
                baseUrl,
                code,
                state
        );
        return  redirectUrl;
    }

}
