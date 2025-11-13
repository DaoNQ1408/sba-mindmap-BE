package com.sbaproject.sbamindmap.dto.request;

import lombok.Data;

@Data
public class WalletDepositRequest {
    private Long userId;
    private Double amount;
    private String paymentMethodName; // "Wallet", "VNPay", "MoMo", etc.
}

