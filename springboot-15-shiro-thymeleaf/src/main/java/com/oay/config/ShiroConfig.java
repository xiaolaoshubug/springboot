package com.oay.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description：shiro配置
 ******************************************/
@Configuration
public class ShiroConfig {

    //配置用户安全管理
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultSecurityManager defaultSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultSecurityManager);
        /**
         * 添加shiro的内置过滤器
         * anon：无须认证就可以访问
         * authc：必须认证了才可以访问
         * perms：拥有对某个资源的权限才能访问
         * role: 拥有某个角色才可以访问
         */
        Map<String, String> filterMap = new LinkedMap();
        /*filterMap.put("/user/add","authc");
        filterMap.put("/user/update","authc");
        filterMap.put("/user/index","anon");*/
        //权限访问，没有授权跳转到授权页面
        filterMap.put("/user/add", "perms[user:add]");
        filterMap.put("/user/update", "perms[user:update,user:add]");
        //所有user下的页面都需要认证
        filterMap.put("/user/*", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //设置登陆页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //设置未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
        return shiroFilterFactoryBean;
    }

    //定义安全管理
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //定义Realm对象
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //整合ShiroDialect，用来整合thymeleaf、shiro
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
