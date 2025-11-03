package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.CreateTransactionRequest;
import com.sbaproject.sbamindmap.dto.response.PaymentInitResponse;
import com.sbaproject.sbamindmap.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/vnpay/init")
    public PaymentInitResponse initVnPay(@RequestBody CreateTransactionRequest request) {
        return paymentService.initVnPayPayment(request);
    }

    @GetMapping("/vnpay/callback")
    public String handleVnPayCallback(
            @RequestParam Map<String, String> params
    ) {
        return paymentService.handleVnPayCallback(params);
    }

}
