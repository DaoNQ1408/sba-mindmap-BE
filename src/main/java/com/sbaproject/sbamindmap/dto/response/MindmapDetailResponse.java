package com.sbaproject.sbamindmap.dto.response;

import com.sbaproject.sbamindmap.enums.SharedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO chứa đầy đủ thông tin để render mindmap trên ReactFlow
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MindmapDetailResponse {
    // Thông tin mindmap cơ bản
    private Long mindmapId;
    private String name;
    private SharedStatus sharedStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dữ liệu từ GeneratedData để render ReactFlow
    private String nodes;           // JSON string của nodes
    private String edges;           // JSON string của edges
    private String knowledgeJson;   // JSON string của knowledge map

    // Thông tin template
    private Long templateId;
    private String templateName;
    private String templateDescription;

    // Thông tin collection
    private Long collectionId;
    private String collectionName;
}
