package com.sbaproject.sbamindmap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponse {
    private String assistantContent;
    private Long conversationId;
    private Long generatedDataId; // nullable
}

