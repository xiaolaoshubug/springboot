package com.oay.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: AsyncService.java
 * @Description： 异步
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-11
 *********************************************************/
@Async      //  告诉Spring这是一个异步方法，注解生效还需要在主线程加上@EnableAsync 开启注解
@Service    //  添加到Spring容器
public class AsyncService {

    public void hello() {
        try {
            System.out.println("业务进行中...");
            Thread.sleep(3000);
            System.out.println("业务结束...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
