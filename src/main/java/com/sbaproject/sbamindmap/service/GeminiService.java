package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.config.GeminiConfig;

public interface GeminiService {
    String generateText(String prompt);
    String generateWithSystem(String systemInstruction, String userPrompt);
}
