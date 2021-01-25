package com.oay.mapper;

import com.oay.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description：描述
 ******************************************/
@Mapper
@Repository
public interface UserMapper {

    User queryUserByName(String name);
}
