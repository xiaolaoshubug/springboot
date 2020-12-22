package com.oay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/*********************************************************
 * @Package: com.oay.config
 * @ClassName: SwaggerConfig.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket1() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("A");
    }

    @Bean
    public Docket docket2() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("B");
    }

    @Bean
    public Docket docket(Environment environment) {

        //  获取当前环境是否为 dev test
        Profiles profiles = Profiles.of("dev", "test");

        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("oay")
                .enable(flag)  //  是否启动swagger
                .select()
                /**
                 * RequestHandlerSelectors
                 .basePackage("com.oay.controller") 指定要扫描的包
                 .any() //  扫描全部
                 .none() // 不扫描
                 */
                .apis(RequestHandlerSelectors.basePackage("com.oay.controller"))
                //  过滤api以外的所有请求
//                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("aoyio", "http://baidu.com/联系人访问链接", "aoyio@qq.com");
        return new ApiInfo(
                "Swagger学习", // 标题
                "学习演示如何配置Swagger", // 描述
                "v1.0", // 版本
                "http://terms.service.url", // 组织链接
                contact, // 联系人信息
                "Apach 2.0 许可", // 许可
                "http://httpd.apache.org/", // 许可连接
                new ArrayList<>()// 扩展
        );
    }
}
