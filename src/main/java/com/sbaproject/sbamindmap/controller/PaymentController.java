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

    /**
     * Khởi tạo thanh toán nạp tiền vào ví
     */
    @PostMapping("/vnpay/init")
    public PaymentInitResponse initVnPay(@RequestBody CreateTransactionRequest request) {
        return paymentService.initVnPayPayment(request);
    }

    /**
     * Khởi tạo thanh toán cho Order
     * User tạo order trước, sau đó thanh toán
     */
    @PostMapping("/order/{orderId}/init")
    public PaymentInitResponse initOrderPayment(@PathVariable Long orderId) {
        return paymentService.initOrderPayment(orderId);
    }

    /**
     * Callback từ VNPay sau khi thanh toán
     */
    @GetMapping("/vnpay/callback")
    public String handleVnPayCallback(
            @RequestParam Map<String, String> params
    ) {
        return paymentService.handleVnPayCallback(params);
    }

}
