package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.ProcessOrderPaymentRequest;
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

    /**
     * Xử lý thanh toán order từ wallet
     * - Tạo transaction nối order với wallet
     * - Trừ tiền từ wallet
     * - Nếu thành công thì chuyển order status sang ACTIVE
     * @param request chứa orderId và walletId để nối 2 entity
     */
    TransactionResponse processOrderPayment(ProcessOrderPaymentRequest request);
}

