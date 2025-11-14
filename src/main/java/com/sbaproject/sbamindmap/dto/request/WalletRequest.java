package com.sbaproject.sbamindmap.dto.request;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WalletRequest {
    private Double balance = 0.0;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
