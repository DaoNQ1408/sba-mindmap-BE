package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.response.TransactionResponse;
import com.sbaproject.sbamindmap.entity.Transaction;
import com.sbaproject.sbamindmap.entity.Wallet;
import com.sbaproject.sbamindmap.repository.TransactionRepository;
import com.sbaproject.sbamindmap.repository.WalletRepository;
import com.sbaproject.sbamindmap.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    /**
     * Lấy lịch sử giao dịch theo wallet ID
     */
    @Override
    public List<TransactionResponse> getTransactionsByWalletId(Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWallet_WalletId(walletId);
        return transactions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy lịch sử giao dịch theo user ID
     */
    @Override
    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user id: " + userId));

        return getTransactionsByWalletId(wallet.getWalletId());
    }

    /**
     * Lấy chi tiết giao dịch
     */
    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionId));

        return convertToResponse(transaction);
    }

    /**
     * Convert Transaction entity sang TransactionResponse DTO
     */
    private TransactionResponse convertToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getWallet().getWalletId(),
                transaction.getPaymentMethod().getName(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getOrder() != null ? transaction.getOrder().getOrderId() : null
        );
    }
}

