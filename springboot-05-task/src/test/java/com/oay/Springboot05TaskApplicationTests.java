package com.oay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class Springboot05TaskApplicationTests {


    @Autowired
    private JavaMailSenderImpl mailSender;  //  注入mailSender

    @Test
    void contextLoads() {
        //邮件设置1：一个简单的邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("通知");
        message.setText("今天下午不用上班");

        message.setTo("aoyio@qq.com");          //  接收
        message.setFrom("354191718@qq.com");    //  发送
        mailSender.send(message);
    }

    @Test
    void contextLoads2() throws MessagingException {
        //邮件设置2：一个复杂的邮件
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,true);

        helper.setSubject("通知");
        //  支持html,true开启
        helper.setText("<h1 style='color:red'>今天下午不用上班</h1>",true);
        //  发送附件
        helper.addAttachment("xiaolaos.jpg", new File("D:/upload/xiaolaos.jpg"));

        helper.setTo("aoyio@qq.com");          //  接收
        helper.setFrom("354191718@qq.com");    //  发送

        mailSender.send(mimeMailMessage);
    }

}
