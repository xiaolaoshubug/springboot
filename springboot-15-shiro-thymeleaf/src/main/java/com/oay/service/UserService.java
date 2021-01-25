package com.oay.service;

import com.oay.entity.User;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description：描述
 ******************************************/
public interface UserService {
    User queryUserByName(String name);
}
