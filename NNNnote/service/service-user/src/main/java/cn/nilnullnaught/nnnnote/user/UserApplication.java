package cn.nilnullnaught.nnnnote.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.nilnullnaught.nnnnote")
@EnableFeignClients //开启 Feign 功能
@MapperScan("cn.nilnullnaught.nnnnote.user.mapper")
//spring cloud 从 Edgware 版本开始，只要配置好注册中心的相关配置即可自动开启服务注册功能
//@EnableDiscoveryClient
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
