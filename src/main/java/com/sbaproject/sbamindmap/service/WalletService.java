package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.WalletDepositRequest;
import com.sbaproject.sbamindmap.dto.request.WalletRequest;
import com.sbaproject.sbamindmap.dto.response.WalletResponse;
import com.sbaproject.sbamindmap.entity.Wallet;

public interface WalletService {
    /**
     * Lấy thông tin ví theo user ID
     */
    WalletResponse getWalletByUserId(Long userId);

    /**
     * Tạo ví mới cho user
     */
    Wallet createWallet(Long userId);

    /**
     * Nạp tiền vào ví
     */
    WalletResponse depositToWallet(WalletDepositRequest request);

    /**
     * Kiểm tra số dư ví
     */
    Double getBalance(Long userId);
}

