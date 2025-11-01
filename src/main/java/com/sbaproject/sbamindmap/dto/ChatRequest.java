package com.sbaproject.sbamindmap.dto;

import com.sbaproject.sbamindmap.enums.MindmapTemplateType;
import lombok.Data;

@Data
public class ChatRequest {
    private String prompt;
    private String systemInstruction;
    private MindmapTemplateType templateType;
}

