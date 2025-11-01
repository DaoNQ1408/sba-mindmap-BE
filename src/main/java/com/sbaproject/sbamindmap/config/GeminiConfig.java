package com.sbaproject.sbamindmap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {
    @Value("${google.gemini.api-key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

}
