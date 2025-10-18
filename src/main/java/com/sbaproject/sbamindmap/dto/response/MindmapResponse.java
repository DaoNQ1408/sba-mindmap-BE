package com.sbaproject.sbamindmap.dto.response;

import com.sbaproject.sbamindmap.enums.SharedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MindmapResponse {
    private Long id;
    private String name;
    private SharedStatus sharedStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long collectionId;
    private Long templateId;
}
