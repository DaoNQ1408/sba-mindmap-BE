package com.sbaproject.sbamindmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private Long conversationId;
    private String role; // "user" or "assistant"
    private String content;
    private Instant createdAt;
    private MindmapDataResponse generatedData; // Null nếu không có dữ liệu mindmap
}

