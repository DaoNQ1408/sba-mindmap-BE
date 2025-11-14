package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.WalletDepositRequest;
import com.sbaproject.sbamindmap.dto.response.WalletResponse;
import com.sbaproject.sbamindmap.entity.Wallet;
import com.sbaproject.sbamindmap.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Wallet Management APIs")
public class WalletController {

    private final WalletService walletService;

    /**
     * Lấy thông tin ví theo user ID
     * GET /api/wallets/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get wallet by user ID", description = "Retrieve wallet information for a specific user")
    public ResponseEntity<WalletResponse> getWalletByUserId(@PathVariable Long userId) {
        WalletResponse wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    /**
     * Tạo ví mới cho user
     * POST /api/wallets/create/{userId}
     */
    @PostMapping("/create/{userId}")
    @Operation(summary = "Create wallet", description = "Create a new wallet for a user")
    public ResponseEntity<Wallet> createWallet(@PathVariable Long userId) {
        Wallet wallet = walletService.createWallet(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    /**
     * Nạp tiền vào ví
     * POST /api/wallets/deposit
     */
    @PostMapping("/deposit")
    @Operation(summary = "Deposit to wallet", description = "Add money to user's wallet")
    public ResponseEntity<WalletResponse> depositToWallet(@RequestBody WalletDepositRequest request) {
        WalletResponse wallet = walletService.depositToWallet(request);
        return ResponseEntity.ok(wallet);
    }

    /**
     * Kiểm tra số dư ví
     * GET /api/wallets/balance/{userId}
     */
    @GetMapping("/balance/{userId}")
    @Operation(summary = "Get wallet balance", description = "Get current balance of user's wallet")
    public ResponseEntity<Double> getBalance(@PathVariable Long userId) {
        Double balance = walletService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }
}

