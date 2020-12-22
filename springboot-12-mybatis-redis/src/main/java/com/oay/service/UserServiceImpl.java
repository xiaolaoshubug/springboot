package com.oay.service;

import com.oay.entity.User;
import com.oay.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: UserServiceImpl.java
 * @Description： 服务实现类
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-23
 *********************************************************/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    //  value可以匹配多个缓存对象，key可以使用SpEL编写，结果可以为null
    @Cacheable(value = {"queryById"}, key = "#id", unless = "#result == null")
    public User queryById(Integer id) {
        return userMapper.queryById(id);
    }

    @Override
    @Cacheable(value = "queryAll")
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    @CachePut(value = "queryById", key = "#user.id")
    public User updateUser(User user) {
        userMapper.updateUser(user);
        return userMapper.queryById(user.getId());
    }

}
