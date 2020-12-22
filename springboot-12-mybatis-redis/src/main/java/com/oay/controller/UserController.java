package com.oay.controller;

import com.oay.entity.User;
import com.oay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/*********************************************************
 * @Package: com.oay.controller
 * @ClassName: UserController.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-23
 *********************************************************/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String hello() {
        return "hello spring";
    }

    @GetMapping("/get/{id}")
    public User queryById(@PathVariable("id") Integer id) {
        return userService.queryById(id);
    }

    @GetMapping("/get")
    public List<User> queryAll() {
        return userService.queryAll();
    }

    //  这里为了方便测试使用了get，操作数据一定要用post
    @GetMapping("/update")
    public User updateUserById() {
        //  伪造一个user的修改数据
        User user = new User();
        user.setId(4).setName("学习redis使用Spring Cache");
        return userService.updateUser(user);
    }
}
