package com.sbaproject.sbamindmap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitResponse {
    private String redirectUrl;
    private String transactionCode;
    private String message;
}

