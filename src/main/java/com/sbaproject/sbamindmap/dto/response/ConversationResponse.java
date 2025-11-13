package com.sbaproject.sbamindmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private Long conversationId;
    private String title;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Long userId;
    private Long apiKeyId;
    private List<MessageResponse> messages;
    private Integer messageCount; // Số lượng tin nhắn trong conversation
}
