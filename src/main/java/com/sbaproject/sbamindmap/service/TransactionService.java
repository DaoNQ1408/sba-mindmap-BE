package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    /**
     * Lấy lịch sử giao dịch theo wallet ID
     */
    List<TransactionResponse> getTransactionsByWalletId(Long walletId);

    /**
     * Lấy lịch sử giao dịch theo user ID
     */
    List<TransactionResponse> getTransactionsByUserId(Long userId);

    /**
     * Lấy chi tiết giao dịch
     */
    TransactionResponse getTransactionById(Long transactionId);
}

