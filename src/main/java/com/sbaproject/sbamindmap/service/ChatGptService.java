package com.sbaproject.sbamindmap.service;

public interface ChatGptService {
    String chat(String prompt);
    String chatWithSystem(String systemInstruction, String userPrompt);
}
