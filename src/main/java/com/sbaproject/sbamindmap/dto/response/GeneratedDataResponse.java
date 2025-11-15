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
public class GeneratedDataResponse {
    private Long generatedDataId;
    private String nodes;
    private String edges;
    private String knowledgeJson;
    private Boolean isChecked;
    private Instant createdAt;
}
