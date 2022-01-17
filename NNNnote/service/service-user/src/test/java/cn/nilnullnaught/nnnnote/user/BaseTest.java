package cn.nilnullnaught.nnnnote.user;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//一般声明在 UT 测试类上，用于指定该测试类的配置类
@ActiveProfiles(value = "test")

@RunWith(SpringRunner.class)
@SpringBootTest//(
//指定启动类
//classes = Application.class,
//和测试类中的 @LocalServerPort 一起在注入属性时使用。会随机生成一个端口号。
//webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//开启事务回滚（防止测试数据污染数据库）
//在需要开启事务回滚的方法上使用 @Transactional
@Rollback(value = true)

//忽略单元测试中不需要的自动配置类
//例：排除 redis 配置类
//@TestPropertySource(properties={
//        "spring.autoconfigure.exclude=" +
//        "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration," +
//        "org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
//})
public class BaseTest {
}
