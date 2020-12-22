package com.oay;

import com.oay.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot11ZookeeperDubboConsumerServiceApplicationTests {


    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        userService.bugTicket();
    }

}
