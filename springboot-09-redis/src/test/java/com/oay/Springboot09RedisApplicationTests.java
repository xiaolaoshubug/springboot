package com.oay;

import com.oay.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class Springboot09RedisApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void contextLoads() {


        //  redisTemplate.opsForValue().set("myRedis","myRedis");
        System.out.println(redisTemplate.opsForValue().get("myRedis"));


    }

}
