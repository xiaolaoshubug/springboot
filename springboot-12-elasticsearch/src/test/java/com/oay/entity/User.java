package com.oay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*********************************************************
 * @Package: com.oay.entity
 * @ClassName: User.java
 * @Description： 用户实体类
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private int age;
}
