package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.config.VnPayConfig;
import com.sbaproject.sbamindmap.dto.request.CreateTransactionRequest;
import com.sbaproject.sbamindmap.dto.response.PaymentInitResponse;
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
import com.sbaproject.sbamindmap.service.PaymentService;
import com.sbaproject.sbamindmap.util.VnPayUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final VnPayConfig vnPayConfig;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;


    // ✅ 1. KHỞI TẠO THANH TOÁN VNPay
    @Override
    @Transactional
    public PaymentInitResponse initVnPayPayment(CreateTransactionRequest req) {

        // ✅ Lấy user trước
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Tìm wallet
        Wallet wallet = walletRepository.findByUserId(req.getUserId())
                .orElseGet(() -> {
                    // ✅ Nếu chưa có thì tạo mới
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(user);
                    newWallet.setBalance(0.0);
                    newWallet.setCreatedAt(LocalDateTime.now());
                    return walletRepository.save(newWallet);
                });

        // ✅ Lấy payment method
        PaymentMethod paymentMethod = paymentMethodRepository
                .findByName("VNPAY")
                .orElseThrow(() -> new RuntimeException("Payment method not found: VNPAY"));

        // ✅ Lưu transaction PENDING
        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setAmount(req.getAmount());
        tx.setStatus(TransactionStatus.PENDING);
        tx.setType(TransactionType.DEPOSIT);
        tx.setCreatedAt(LocalDateTime.now());
        tx.setPaymentMethod(paymentMethod); // ✅ BẮT BUỘC

        transactionRepository.save(tx);


        String txnRef = String.valueOf(tx.getTransactionId());
        long amount = (long) (req.getAmount() * 100);
        String orderInfo = "Nap tien vao vi #" + wallet.getWalletId();

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_CreateDate", VnPayUtils.getCurrentTime());
        params.put("vnp_ExpireDate", VnPayUtils.getExpireTime(15));

        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {

            if (query.length() > 0) query.append("&");
            if (hashData.length() > 0) hashData.append("&");

            String key = entry.getKey();
            String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);

            hashData.append(key).append("=").append(value);
            query.append(key).append("=").append(value);
        }

        String secureHash = VnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        String paymentUrl = vnPayConfig.getPayUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;

        return new PaymentInitResponse(paymentUrl, txnRef, "VNPay initialized");
    }


    // ✅ 2. CALLBACK VNPay
    @Override
    @Transactional
    public String handleVnPayCallback(Map<String, String> params) {

        String secureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        Map<String, String> sorted = new TreeMap<>(params);

        // ✅ Encode giống hệt lúc init
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (hashData.length() > 0) hashData.append("&");
            hashData
                    .append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        String calculatedHash = VnPayUtils.hmacSHA512(
                vnPayConfig.getHashSecret(),
                hashData.toString()
        );

        if (!secureHash.equals(calculatedHash)) {
            System.out.println("Expected: " + secureHash);
            System.out.println("Got: " + calculatedHash);
            return "INVALID HASH";
        }

        // ✅ Lấy transaction
        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");

        Transaction tx = transactionRepository.findById(Long.parseLong(txnRef))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("00".equals(responseCode)) {
            tx.setStatus(TransactionStatus.SUCCESS);

            Wallet wallet = tx.getWallet();
            wallet.setBalance(wallet.getBalance() + tx.getAmount());
            walletRepository.save(wallet);
        } else {
            tx.setStatus(TransactionStatus.FAILED);
        }

        transactionRepository.save(tx);

        return "OK";
    }

}
