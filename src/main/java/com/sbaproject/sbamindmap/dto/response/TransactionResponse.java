package com.sbaproject.sbamindmap.dto.response;

import com.sbaproject.sbamindmap.enums.TransactionStatus;
import com.sbaproject.sbamindmap.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long transactionId;
    private Long walletId;
    private String paymentMethodName;
    private TransactionType type;
    private TransactionStatus status;
    private Double amount;
    private LocalDateTime createdAt;
    private Long orderId;
}

