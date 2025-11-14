package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.CreateTransactionRequest;
import com.sbaproject.sbamindmap.dto.response.PaymentInitResponse;

import java.util.Map;

public interface PaymentService {
    PaymentInitResponse initVnPayPayment(CreateTransactionRequest request);
    String handleVnPayCallback(Map<String, String> params);
    PaymentInitResponse initOrderPayment(Long orderId);
}
