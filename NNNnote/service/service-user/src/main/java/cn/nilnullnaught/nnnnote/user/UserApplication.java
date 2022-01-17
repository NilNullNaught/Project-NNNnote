package cn.nilnullnaught.nnnnote.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.nilnullnaught.nnnnote")
@EnableFeignClients("cn.nilnullnaught.nnnnote.client") //开启 Feign 功能
@MapperScan("cn.nilnullnaught.nnnnote.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
