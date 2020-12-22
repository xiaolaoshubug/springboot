package com.oay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*********************************************************
 * @Package: com.oay.entity
 * @ClassName: User.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-12
 *********************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private String name;
    private String password;
}
