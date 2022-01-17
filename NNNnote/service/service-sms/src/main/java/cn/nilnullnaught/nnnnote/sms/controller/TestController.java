package cn.nilnullnaught.nnnnote.sms.controller;

import cn.nilnullnaught.nnnnote.common.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("sms/test")
public class TestController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation("根据 key 从 Redis 中获取对应的 Value")
    @GetMapping("getRedisValue/{key}")
    public R getRedisValueTest(@PathVariable String key) {
        String value = redisTemplate.opsForValue().get(key);
        return R.ok().data("data",value);
    }

}
