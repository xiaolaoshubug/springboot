package com.oay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.oay.mapper")
public class Springboot07MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot07MybatisApplication.class, args);
    }

}
