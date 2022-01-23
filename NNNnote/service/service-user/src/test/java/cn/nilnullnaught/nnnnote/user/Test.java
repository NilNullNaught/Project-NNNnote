package cn.nilnullnaught.nnnnote.user;

import cn.nilnullnaught.nnnnote.common.utils.JwtUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;

public class Test {
    @org.junit.Test
    public void t(){
        String jwtToken = JwtUtils.getJwtToken("1231", true);
        System.out.println(jwtToken);
    }
    @org.junit.Test
    public void main1() {
        String url = "jdbc:mysql://8.131.72.52:3306/NNNnote?useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true";
        String username = "Outsider";
        String password = "PASSWORDis1024";
        // 设置作者
        String author = "NilNullNaught";
        // 指定输出目录
        String outputDir = System.getProperty("user.dir")+"/src/main/java";
        // 设置父包名
        String parent = "cn.nilnullnaught.nnnnote";
        //设置父包模块名
        String moduleName = "user";
        //设置mapperXml生成路径
        String mapperXmlDir = System.getProperty("user.dir")+"/src/main/java"+"/cn/nilnullnaught/nnnnote/" +moduleName+"/mapper/xml" ;
        // 设置需要生成的表名
        String[] tables = {"user_info"};
        // 设置过滤表前缀
        String[] removePrefix = {"t_", "c_"};

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            //.fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(parent) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperXmlDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix(removePrefix) // 设置过滤表前缀
                            .entityBuilder() // Entity 策略配置
                            .enableLombok() // 开启 Lombok
                            .controllerBuilder() // Controller 策略配置
                            .enableRestStyle() // 开启生成@RestController 控制器
                            .serviceBuilder() // Service 策略配置
                            .formatServiceFileName("%sService") // 去掉字母 I
                            .formatServiceImplFileName("%sServiceImpl")
                            .mapperBuilder() // Mapper 策略配置
                            .enableMapperAnnotation(); // 开启 @Mapper 注解
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板(需要导包)，默认的是Velocity引擎模板
                .execute();
    }


    @org.junit.Test
    public void t2(){
        String oldUrl =
                "https://nilnullnaught-nnnnote-01.oss-cn-shanghai.aliyuncs.com/temporary/2022/01/23/67b5ab7f7b754416921bc8501969bfee.jpeg";

            String newUrl = oldUrl.replace("/temporary","");

        System.out.println("oldUrl = " + oldUrl);
        System.out.println("newUrl = " + newUrl);
    }
}
