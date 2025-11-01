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
public class MessageResponse {
    private Long messageId;
    private String role;
    private String content;
    private String metadata;
    private Instant createdAt;
    private GeneratedDataResponse generatedData;
}

