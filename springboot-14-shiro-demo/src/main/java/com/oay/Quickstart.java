package com.oay;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Quickstart application showing how to use Shiro's API.
 *
 * @since 0.9 RC2
 */
public class Quickstart {

    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);


    public static void main(String[] args) {
        //创建带有realms,users,roles和permissions配置的Shiro SecurityManager的最简单方法是使用简单的ini配置
        //我们将使用可提取.ini文件(在类路径的根目录下使用shiro.ini文件)的工厂返回一个SecurityManager实例
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        //对于这个简单的示例快速入门，请使SecurityManager作为JVM单例访问。
        //大多数应用程序都不会这样做，而在webapps会依靠其容器配置或web.xml进行使用
        SecurityUtils.setSecurityManager(securityManager);

        //现在已经建立了一个简单的Shiro环境，接下来可以进行一些操作

        //获取当前执行的用户subject
        Subject currentUser = SecurityUtils.getSubject();

        //通过当前用户得到session,使用Session做一些事情（不需要Web或EJB容器）
        Session session = currentUser.getSession();
        session.setAttribute("someKey", "aValue");//设置值
        String value = (String) session.getAttribute("someKey");//获取值
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }

        //让我们登录当前用户，以便我们可以检查角色和权限
        //测试当前用户是否被认证
        if (!currentUser.isAuthenticated()) {//如果没有被认证
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");//生成一个Token令牌,随机设置
            token.setRememberMe(true);//设置记住我
            try {
                currentUser.login(token);//执行登录操作
            } catch (UnknownAccountException uae) {//未知的账户
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {//证书不正确
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {//用户被锁定
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            //...还可以捕获更多异常（也许是针对您的应用程序的自定义异常）
            catch (AuthenticationException ae) {
                //意外状况？错误？
            }
        }

        //打印当前用户的主要身份信息(本案例中是用户名)
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

        //测试角色
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

        //测试权限（不是实例级别:粗粒度）
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        //(非常强大的)实例级别权限:细粒度
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //注销
        currentUser.logout();

        //结束系统
        System.exit(0);
    }
}
