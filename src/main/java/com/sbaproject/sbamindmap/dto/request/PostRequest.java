package com.sbaproject.sbamindmap.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long communityId;
    private Long userId;
    private String title;
    private String content;
}
