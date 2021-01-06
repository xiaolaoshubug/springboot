package com.oay.controller;

import com.oay.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/*********************************************************
 * @Package: com.oay.controller
 * @ClassName: IndexController.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/
@RestController
public class IndexController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) {
        return contentService.parse(keyword);
    }

    @GetMapping("/searchPage/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> searchPage(
            @PathVariable("keyword") String keyword,
            @PathVariable("pageNo") int pageNo,
            @PathVariable("pageSize") int pageSize) {

        if (pageNo <= 1) {
            pageNo = 1;
        }
        return contentService.searchPage(keyword, pageNo, pageSize);
    }

}
