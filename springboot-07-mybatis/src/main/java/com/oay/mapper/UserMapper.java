package com.oay.mapper;

import com.oay.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/*********************************************************
 * @Package: com.oay.mapper
 * @ClassName: UserMapper.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@Repository
public interface UserMapper {
    List<User> queryAll();
}
