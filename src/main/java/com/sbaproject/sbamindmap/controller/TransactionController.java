package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.ProcessOrderPaymentRequest;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.dto.response.TransactionResponse;
import com.sbaproject.sbamindmap.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Xử lý thanh toán order từ wallet
     * POST /api/transactions/process-payment
     * Body: { "orderId": 1, "walletId": 1 }
     */
    @PostMapping("/process-payment")
    public ResponseEntity<ResponseBase> processOrderPayment(@RequestBody ProcessOrderPaymentRequest request) {
        try {
            TransactionResponse transaction = transactionService.processOrderPayment(request);
            return ResponseEntity.ok(new ResponseBase(200, "Payment processed successfully. Order is now active.", transaction));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ResponseBase(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to process payment: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy lịch sử giao dịch theo wallet ID
     * GET /api/transactions/wallet/{walletId}
     */
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<ResponseBase> getTransactionsByWalletId(@PathVariable Long walletId) {
        try {
            List<TransactionResponse> transactions = transactionService.getTransactionsByWalletId(walletId);
            return ResponseEntity.ok(new ResponseBase(200, "Transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to retrieve transactions: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy lịch sử giao dịch theo user ID
     * GET /api/transactions/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseBase> getTransactionsByUserId(@PathVariable Long userId) {
        try {
            List<TransactionResponse> transactions = transactionService.getTransactionsByUserId(userId);
            return ResponseEntity.ok(new ResponseBase(200, "Transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseBase(404, e.getMessage(), null));
        }
    }

    /**
     * Lấy chi tiết giao dịch theo ID
     * GET /api/transactions/{transactionId}
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<ResponseBase> getTransactionById(@PathVariable Long transactionId) {
        try {
            TransactionResponse transaction = transactionService.getTransactionById(transactionId);
            return ResponseEntity.ok(new ResponseBase(200, "Transaction retrieved successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseBase(404, e.getMessage(), null));
        }
    }
}

