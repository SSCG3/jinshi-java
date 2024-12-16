// src/main/java/com/jinshi/service/EmailService.java
package com.jinshi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {
    private final static String MyEmail = "zhouquanxi01@163.com";//开启授权码的邮箱
    private final static String AuthorizationCode = "NYmTqBrLMM4qUpJ9";//授权码
    private final static String SMTPEmail = "smtp.163.com";
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public boolean sendEmail(String toEmail, String subject, String content,
                             String senderName, String receiverName) {
        logger.info("开始发送邮件到: {}", toEmail);

        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", SMTPEmail);
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.ssl.enable", "true");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MyEmail, AuthorizationCode);
                }
            });
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MyEmail, senderName, "UTF-8"));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail, receiverName, "UTF-8"));
            message.setRecipient(Message.RecipientType.CC,
                    new InternetAddress(MyEmail, senderName, "UTF-8"));
            message.setSubject(subject.trim(), "UTF-8");
            message.setContent(content.trim(), "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();

            Transport transport = session.getTransport();
            transport.connect(MyEmail, AuthorizationCode);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            logger.info("邮件发送成功!");
            return true;
        } catch (Exception e) {
            logger.error("发送邮件时发生错误: ", e);
            return false;
        }
    }
}