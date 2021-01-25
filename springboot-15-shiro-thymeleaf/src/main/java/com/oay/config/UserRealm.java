package com.oay.config;

import com.oay.entity.User;
import com.oay.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description： 自定义Realm(用户授权 ， 用户认证)
 ******************************************/
public class UserRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("执行了授权====>doGetAuthorizationInfo方法");
        //授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前用户登录对象
        Subject subject = SecurityUtils.getSubject();
        //拿到user对象
        User currentUser = (User) subject.getPrincipal();
        //添加授权用户
        info.addStringPermission(currentUser.getPerms());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("执行了认证====>doGetAuthenticationInfo方法");
        //用户名，密码，检验数据库用户
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //用户实例信息
        User user = userService.queryUserByName(userToken.getUsername());
        if (user == null) {
            return null; // null抛出异常
        }
        //用户登录成功后添加到session中
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser", user);
        //密码认证，不需要自己去认证，shiro封装了密码认证
        //可以使用自己定义密码认证
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");
    }
}
