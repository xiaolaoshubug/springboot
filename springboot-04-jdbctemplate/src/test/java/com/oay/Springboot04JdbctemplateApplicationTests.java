package com.oay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
class Springboot04JdbctemplateApplicationTests {


    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
    }

}
