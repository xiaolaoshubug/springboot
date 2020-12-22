package com.oay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*********************************************************
 * @Package: com.oay.controller
 * @ClassName: HelloController.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-06
 *********************************************************/
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
