package com.oay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*********************************************************
 * @Package: com.oay.entity
 * @ClassName: User.java
 * @Description： 用户实体类
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-23
 *********************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)    //  链式写法
public class User implements Serializable {

    private Integer id;
    private String name;
    private String pwd;
}
