package com.oay.service;

import com.oay.entity.User;
import com.oay.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description：描述
 ******************************************/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }
}
