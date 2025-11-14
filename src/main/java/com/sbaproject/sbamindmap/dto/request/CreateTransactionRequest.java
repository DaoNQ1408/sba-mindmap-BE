package com.sbaproject.sbamindmap.dto.request;

import lombok.Data;

@Data
public class CreateTransactionRequest {
    private Long userId;
    private Double amount;
    private String currency;
    private String paymentMethodName;
    private String description;
}
