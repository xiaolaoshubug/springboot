package com.oay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/******************************************
 * @author：ouay
 * @Version：v1.0
 * @Date：2021-01-18
 * @Description：描述
 ******************************************/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private int id;
    private String name;
    private String pwd;
    private String perms;
}
