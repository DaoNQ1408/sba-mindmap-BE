package com.sbaproject.sbamindmap.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageRequest {

    private String name;
    private double price;
    private String apiCallLimit;
    private LocalDateTime durationDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
