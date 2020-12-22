package com.oay.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*********************************************************
 * @Package: com.oay.pojo
 * @ClassName: User.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户实体类")
public class User implements Serializable {

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;
}
