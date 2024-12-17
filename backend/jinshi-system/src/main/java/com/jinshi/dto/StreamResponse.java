// src/main/java/com/jinshi/controller/StreamResponse.java

package com.jinshi.dto;

public class StreamResponse {
    private String content;

    public StreamResponse() {
    }

    public StreamResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
