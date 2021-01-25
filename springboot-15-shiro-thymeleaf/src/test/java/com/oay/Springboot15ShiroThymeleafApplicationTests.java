package com.oay;

import com.oay.entity.User;
import com.oay.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot15ShiroThymeleafApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void contextLoads() {
        User user = userService.queryUserByName("root");
        System.out.println(user);
    }

}
