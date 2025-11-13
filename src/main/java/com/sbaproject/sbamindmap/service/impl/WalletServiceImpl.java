package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.WalletDepositRequest;
import com.sbaproject.sbamindmap.dto.response.WalletResponse;
import com.sbaproject.sbamindmap.entity.PaymentMethod;
import com.sbaproject.sbamindmap.entity.Transaction;
import com.sbaproject.sbamindmap.entity.User;
import com.sbaproject.sbamindmap.entity.Wallet;
import com.sbaproject.sbamindmap.enums.TransactionStatus;
import com.sbaproject.sbamindmap.enums.TransactionType;
import com.sbaproject.sbamindmap.repository.PaymentMethodRepository;
import com.sbaproject.sbamindmap.repository.TransactionRepository;
import com.sbaproject.sbamindmap.repository.UserRepository;
import com.sbaproject.sbamindmap.repository.WalletRepository;
import com.sbaproject.sbamindmap.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    /**
     * Lấy thông tin ví theo user ID
     */
    @Override
    public WalletResponse getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user id: " + userId));

        return convertToResponse(wallet);
    }

    /**
     * Tạo ví mới cho user
     */
    @Override
    @Transactional
    public Wallet createWallet(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Kiểm tra xem user đã có ví chưa
        if (walletRepository.findByUser_Id(userId).isPresent()) {
            throw new IllegalStateException("User already has a wallet");
        }

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);
        wallet.setCreatedAt(LocalDateTime.now());

        return walletRepository.save(wallet);
    }

    /**
     * Nạp tiền vào ví
     */
    @Override
    @Transactional
    public WalletResponse depositToWallet(WalletDepositRequest request) {
        // Validate
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }

        // Tìm hoặc tạo ví
        Wallet wallet = walletRepository.findByUser_Id(request.getUserId())
                .orElseGet(() -> createWallet(request.getUserId()));

        // Cập nhật số dư
        wallet.setBalance(wallet.getBalance() + request.getAmount());
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // Lấy payment method
        String paymentMethodName = request.getPaymentMethodName() != null ?
                request.getPaymentMethodName() : "Wallet";
        PaymentMethod paymentMethod = paymentMethodRepository.findByName(paymentMethodName)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found: " + paymentMethodName));

        // Tạo transaction record
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        return convertToResponse(wallet);
    }

    /**
     * Kiểm tra số dư ví
     */
    @Override
    public Double getBalance(Long userId) {
        Wallet wallet = walletRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user id: " + userId));
        return wallet.getBalance();
    }

    /**
     * Convert Wallet entity sang WalletResponse DTO
     */
    private WalletResponse convertToResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getWalletId(),
                wallet.getUser().getId(),
                wallet.getBalance(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}

