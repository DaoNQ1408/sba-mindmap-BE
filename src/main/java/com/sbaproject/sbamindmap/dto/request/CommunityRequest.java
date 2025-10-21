package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.CommunityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityRequest {
    private Long userId;
    private String name;
    private CommunityStatus status;
}

