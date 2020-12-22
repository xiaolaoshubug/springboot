package com.oay.controller;

import com.oay.entity.User;
import com.oay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*********************************************************
 * @Package: com.oay.controller
 * @ClassName: UserController.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/all")
    public List<User> queryAll() {
        return userService.queryAll();
    }
}
