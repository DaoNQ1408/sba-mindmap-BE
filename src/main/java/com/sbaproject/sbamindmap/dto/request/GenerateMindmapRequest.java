package com.sbaproject.sbamindmap.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class GenerateMindmapRequest {
    private String topic; // Chủ đề toán học
    private String grade; // Khối lớp: "10", "11", "12"
    private Long templateId; // ID của template
    private Long conversationId; // Null nếu tạo mới conversation
    private String userId; // Giả lập user ID, ví dụ: "test-user"
}


