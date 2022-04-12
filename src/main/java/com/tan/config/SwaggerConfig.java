package com.tan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


@Configuration
@EnableSwagger2           //开启swagger2
public class SwaggerConfig {
    //配置了swagger的Docket的bean实例
    @Bean
    public Docket docket(Environment environment){

        //设置要使用的环境中
        Profiles profiles = Profiles.of("dev");
        //通过方法  判断自己项目所使用的的环境  在不在这里的指定的环境里 如果在就能访问的到 不在就访问不到
        boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)//通过方法  判断自己项目所使用的的环境  在不在这里的指定的环境里 如果在就能访问的到 不在就访问不到
                .select()//选择
                //可以扫描 any none 指定包  指定类  指定方法
                .apis(RequestHandlerSelectors.withMethodAnnotation(GetMapping.class))
//                .paths(PathSelectors.ant("com.example.controller/**")) 过滤不需要扫描的路径
                .build();//创建
    }
    //配置swagger信息  apiinfo
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("可心的swagger文档")
                .description("我想认真读一本书")
                .version("v3.0")
                .termsOfServiceUrl("https://blog.csdn.net/weixin_58993861?type=blog")
                .contact("程序员")
                .build();

    }
}
