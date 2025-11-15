package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.SharedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request để tạo Mindmap thủ công từ GeneratedData
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMindmapFromDataRequest {
    private Long generatedDataId; // Required
    private Long templateId; // Required
    private String name; // Tên mindmap
    private SharedStatus sharedStatus; // PRIVATE, PUBLIC, SHARED
    private Long collectionId; // Optional - nếu null sẽ tạo collection mới
}

