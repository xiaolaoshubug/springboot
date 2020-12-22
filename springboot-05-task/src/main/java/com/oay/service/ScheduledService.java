package com.oay.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: ScheduledService.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@Service
public class ScheduledService {

    //秒   分   时     日   月   周几
    @Scheduled(cron = "0/2 * * * * ?")  //  每两秒执行一次
    public void hello() {
        System.out.println("hello.....");
    }

}
