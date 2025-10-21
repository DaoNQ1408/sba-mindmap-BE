package com.sbaproject.sbamindmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionResponse {
    private Long id;
    private String name;
    private String sharedStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
