package com.oay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.oay.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot12MybatisRedisApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        Object o = redisUtil.get("queryById::4");
        System.out.println(o);
        /*String str = JSON.toJSONString(o);
        System.out.println(str);*/
        //  转换得到一个数组
        /*JSONArray arrayList = JSONArray.parseArray(str);
        for (int i = 0; i < arrayList.size(); i++) {
            String id = arrayList.getJSONObject(i).getString("id");
            String name = arrayList.getJSONObject(i).getString("name");
            String pwd = arrayList.getJSONObject(i).getString("pwd");
            System.out.println(
                    "id==>"+id+"\t"+
                    "name==>"+name+"\t"+
                    "pwd==>"+pwd
            );
        }*/
    }

}
