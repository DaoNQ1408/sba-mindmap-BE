package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {
    @Autowired
    private GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/generate")
    public String generate(@RequestParam String prompt) {
        return geminiService.generateText(prompt);
    }

    @PostMapping("/generate/system")
    public String generateWithSystem(@RequestParam String system, @RequestParam String prompt) {
        return geminiService.generateWithSystem(system, prompt);
    }
}
