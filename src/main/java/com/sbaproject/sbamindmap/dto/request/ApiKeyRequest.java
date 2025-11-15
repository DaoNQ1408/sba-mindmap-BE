package com.sbaproject.sbamindmap.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiKeyRequest {

    private Integer remainingCalls;

    private Boolean isActive = true;

    private Instant activatedAt;

    private Instant expiredAt;
}
