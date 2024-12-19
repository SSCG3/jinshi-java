// src/main/java/com/jinshi/controller/DocController.java
package com.jinshi.controller;

import com.jinshi.dto.DocGenerateRequest;
import com.jinshi.service.DocWritingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 开发环境允许跨域
public class DocController {

    private final DocWritingService docWritingService;

    public DocController(DocWritingService docWritingService) {
        this.docWritingService = docWritingService;
    }

    @PostMapping(value = "/generate-doc", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateDoc(@RequestBody DocGenerateRequest request) {
        SseEmitter emitter = new SseEmitter();
        docWritingService.generateDocStream(request, emitter);
        return emitter;
    }
}