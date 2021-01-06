package com.oay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*********************************************************
 * @Package: com.oay.entity
 * @ClassName: Content.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    private String img;
    private String price;
    private String title;
}
