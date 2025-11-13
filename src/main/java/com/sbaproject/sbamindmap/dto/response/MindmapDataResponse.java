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
public class MindmapDataResponse {
    private Long generatedDataId;
    private Long messageId;
    private Long conversationId;
    private String nodesJson; // JSON string của nodes
    private String edgesJson; // JSON string của edges
    private Boolean isChecked;
    private Instant createdAt;
}

