package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.SharedStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest {
    private String name;
    private SharedStatus sharedStatus;
    private Long userId;
}
