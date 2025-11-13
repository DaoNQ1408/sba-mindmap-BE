package com.sbaproject.sbamindmap.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String message; // Nội dung tin nhắn
    private Long conversationId; // ID cuộc hội thoại (null nếu tạo mới)
    private String userId; // Giả lập user ID
}

