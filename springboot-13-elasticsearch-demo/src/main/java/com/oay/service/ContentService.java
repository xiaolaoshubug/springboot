package com.oay.service;

import java.util.List;
import java.util.Map;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: ContentService.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/
public interface ContentService {

    //  条件查询
    Boolean parse(String keyword);

    //  分页查询
    List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize);

}
