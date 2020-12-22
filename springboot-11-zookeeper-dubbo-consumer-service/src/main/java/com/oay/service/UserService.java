package com.oay.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: UserService.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2020-12-13
 *********************************************************/
@Component
public class UserService {

    @Reference
    private TicketService ticketService;

    public void bugTicket() {
        System.out.println(ticketService.getTicket());
    }
}
