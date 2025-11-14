package com.sbaproject.sbamindmap.dto.request;

import lombok.Data;

@Data
public class ProcessOrderPaymentRequest {
    private Long orderId;   // Order cần thanh toán
    private Long walletId;  // Wallet để trừ tiền
}

