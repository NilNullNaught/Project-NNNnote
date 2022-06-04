package cn.nilnullnaught.nnnnote.transit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TransitApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransitApplication.class, args);
    }
}
