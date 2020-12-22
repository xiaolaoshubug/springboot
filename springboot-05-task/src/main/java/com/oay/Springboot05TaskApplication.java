package com.oay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync        //  开启异步注解
@EnableScheduling   //  开启定时任务注解
@SpringBootApplication
public class Springboot05TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot05TaskApplication.class, args);
    }

}
