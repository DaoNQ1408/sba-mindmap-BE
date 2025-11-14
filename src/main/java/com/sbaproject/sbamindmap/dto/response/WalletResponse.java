package com.sbaproject.sbamindmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {
    private Long walletId;
    private Long userId;
    private Double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

