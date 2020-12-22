package com.oay.service;

import com.oay.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: UserService.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@Service
public interface UserService {
    List<User> queryAll();
}
