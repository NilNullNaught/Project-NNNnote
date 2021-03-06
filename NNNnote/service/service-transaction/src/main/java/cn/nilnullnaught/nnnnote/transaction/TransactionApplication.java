package cn.nilnullnaught.nnnnote.transaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.nilnullnaught.nnnnote")
@EnableFeignClients("cn.nilnullnaught.nnnnote.client") //开启 Feign 功能
@MapperScan("cn.nilnullnaught.nnnnote.transaction.mapper")
public class TransactionApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }
}