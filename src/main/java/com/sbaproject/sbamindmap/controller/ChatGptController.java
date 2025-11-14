package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chatgpt")
public class ChatGptController {
    @Autowired
    private ChatGptService chatGptService;

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> body) {
        String prompt = body.get("message");
        String reply = chatGptService.chat(prompt);
        return Map.of("response", reply);
    }
}
