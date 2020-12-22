package com.oay.service;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: TicketServiceImpl.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-12
 *********************************************************/
@Service
@Component  //  这里尽量使用@Component注解注入bean
public class TicketServiceImpl implements TicketService {
    @Override
    public String getTicket() {
        return "学习Springboot+Dubbo+zookeeper";
    }
}
