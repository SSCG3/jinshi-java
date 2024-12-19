// jinshi-java/backend/jinshi-system/src/main/java/com/jinshi/service/DocWritingService.java

package com.jinshi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinshi.dto.DocGenerateRequest;
import com.jinshi.dto.StreamResponse;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DocWritingService {
    private static final String API_KEY = "sfYijvVT0jSJbStJ5c4pOiIh";
    private static final String SECRET_KEY = "MsjRU1KuAVisifJxk711OGjpTa049Mi8";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void generateDocStream(DocGenerateRequest request, SseEmitter emitter) {
        executorService.execute(() -> {
            try {
                String prompt = buildPrompt(request.getRequirements());
                String accessToken = getAccessToken();

                // 构建请求体
                JSONObject requestBody = new JSONObject();
                requestBody.put("messages", new JSONObject[]{
                        new JSONObject().put("role", "user").put("content", prompt)
                });
                requestBody.put("temperature", 0.7);
                requestBody.put("top_p", 0.8);
                requestBody.put("stream", true);  // 启用流式返回

                // 构建请求
                Request httpRequest = new Request.Builder()
                        .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + accessToken)
                        .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                        .build();

                // 执行请求
                try (Response response = HTTP_CLIENT.newCall(httpRequest).execute()) {
                    if (!response.isSuccessful() || response.body() == null) {
                        throw new IOException("Unexpected response " + response);
                    }

                    // 读取流式响应
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                        StringBuilder fullContent = new StringBuilder();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ")) {
                                String jsonData = line.substring(6);
                                JSONObject chunk = new JSONObject(jsonData);

                                if (chunk.has("result")) {
                                    String content = chunk.getString("result");
                                    if (!content.isEmpty()) {
                                        fullContent.append(content);

                                        // 每个响应包装成一个带有完整内容的对象
                                        StreamResponse streamResponse = new StreamResponse(fullContent.toString());
                                        String jsonResponse = objectMapper.writeValueAsString(streamResponse);

                                        // 发送响应并添加延迟
                                        emitter.send("data: " + jsonResponse + "\n\n");
                                    }
                                }

                            }
                        }
                    }
                }

                emitter.complete();

            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
    }

    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,
                "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to get access token");
            }
            return new JSONObject(response.body().string()).getString("access_token");
        }
    }

    private String buildPrompt(String requirements) {
        return String.format("请根据以下要求生成一份工作会议讲话稿:\n" +
                        "%s\n" +
                        "要求:\n" +
                        "1. 直接输出稿件正文,不需要再重复讲话稿的标题\n" +
                        "2. 结构清晰,层次分明\n" +
                        "3. 内容积极向上,富有激励性\n" +
                        "4. 语言严谨规范,符合讲话稿风格",
                requirements);
    }
}