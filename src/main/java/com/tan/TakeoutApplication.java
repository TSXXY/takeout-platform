package com.tan;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author TanS
 * @date 2022/4/12
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.tan.mapper")
public class TakeoutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeoutApplication.class,args);
    }
}
