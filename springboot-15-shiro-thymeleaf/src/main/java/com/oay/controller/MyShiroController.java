package com.oay.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description：描述
 ******************************************/
@Controller
public class MyShiroController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("msg", "hello,shiro");
        return "index";
    }

    @GetMapping("/user/add")
    public String add() {
        return "user/add";
    }

    @GetMapping("/user/update")
    public String update() {
        return "user/update";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户登陆数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //执行登陆方法
        try {
            subject.login(token);
            model.addAttribute("msg", "当前用户：" + username);
            return "index";
        } /*catch (AuthenticationException e) {
            e.printStackTrace();
            model.addAttribute("msg", "用户名或密码错误");
            return "login";
        }*/ catch (UnknownAccountException e) {//用户名不存在
            model.addAttribute("msg", "用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {//密码不存在
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }

    @ResponseBody
    @GetMapping("/noAuth")
    public String unauthorized() {
        return "抱歉，你没有权限";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg", "注销成功");
        return "index";
    }

}
