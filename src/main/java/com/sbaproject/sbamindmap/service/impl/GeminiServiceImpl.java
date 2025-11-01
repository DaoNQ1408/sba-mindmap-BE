package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.config.GeminiConfig;
import com.sbaproject.sbamindmap.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiServiceImpl implements GeminiService {

    private final WebClient webClient;
    private final String apiKey;
    private final String modelId;

    public GeminiServiceImpl(
            @Value("${google.gemini.api-key}") String apiKey,
            @Value("${google.gemini.model-id:gemini-2.5-flash}") String modelId
    ) {
        this.apiKey = apiKey;
        this.modelId = modelId;
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String generateText(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                )
        );

        return callGemini(body);
    }

    @Override
    public String generateWithSystem(String systemInstruction, String userPrompt) {
        Map<String, Object> body = Map.of(
                "system_instruction", Map.of(
                        "role", "system",
                        "parts", List.of(Map.of("text", systemInstruction))
                ),
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", userPrompt))
                        )
                )
        );

        return callGemini(body);
    }

    private String callGemini(Map<String, Object> body) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/models/{model}:generateContent")
                        .queryParam("key", apiKey)
                        .build(modelId))
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("Gemini 4xx: " + msg)))
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("Gemini 5xx: " + msg)))
                .bodyToMono(String.class)
                .block();
    }

}