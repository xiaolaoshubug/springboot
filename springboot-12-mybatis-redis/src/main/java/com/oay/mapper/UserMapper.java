package com.oay.mapper;

import com.oay.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*********************************************************
 * @Package: com.oay.mapper
 * @ClassName: UserMapper.java
 * @Description： mapper接口类
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-23
 *********************************************************/
@Mapper
@Repository
public interface UserMapper {

    User queryById(@Param("id") Integer id);

    List<User> queryAll();

    User updateUser(User user);
}
