package cn.nilnullnaught.nnnnote.user.controller;


import cn.nilnullnaught.nnnnote.common.utils.R;
import cn.nilnullnaught.nnnnote.entity.UserMessage;
import cn.nilnullnaught.nnnnote.user.service.UserMessageService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author nilnullnaught
 * @since 2022-01-12
 */
@Slf4j
@RestController
@RequestMapping("/user/user-message")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    @ApiOperation("mybatis-plus 测试")
    @GetMapping("/getallUser")
    public R getAllUser() {

        List<UserMessage> list = userMessageService.list();

        return R.ok().data("data", list);
    }

    @ApiOperation("异常处理测试")
    @GetMapping("/Exception")
    public R exception() {
        log.info("logback info 成功了");
        log.error("logback error 成功了");
        log.debug("logback debug 成功了");
        return R.ok().data("？？？", 20000);
    }

}
