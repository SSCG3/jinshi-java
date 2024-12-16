// src/main/java/com/jinshi/controller/EmailController.java
package com.jinshi.controller;

import com.jinshi.dto.EmailRequest;
import com.jinshi.dto.MessageResponse;
import com.jinshi.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailRequest request) {
        logger.info("接收到发送邮件请求: {}", request.getToEmail());

        try {
            logger.debug("开始处理邮件发送...");
            boolean success = emailService.sendEmail(
                    request.getToEmail(),
                    request.getSubject(),
                    request.getContent(),
                    request.getSenderName(),
                    request.getReceiverName()
            );

            if (success) {
                logger.info("邮件发送成功");
                return ResponseEntity.ok(new MessageResponse("邮件发送成功"));
            } else {
                logger.error("邮件发送失败");
                return ResponseEntity.internalServerError()
                        .body(new MessageResponse("邮件发送失败"));
            }
        } catch (IllegalArgumentException e) {
            logger.error("请求参数错误: ", e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("发送邮件时发生错误: ", e);
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("发送邮件时发生错误：" + e.getMessage()));
        }
    }
}