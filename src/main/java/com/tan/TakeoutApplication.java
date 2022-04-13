package com.tan;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author TanS
 * @date 2022/4/12
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.tan.mapper")
@ServletComponentScan
@EnableTransactionManagement//开启事务
public class TakeoutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeoutApplication.class,args);
    }
}
