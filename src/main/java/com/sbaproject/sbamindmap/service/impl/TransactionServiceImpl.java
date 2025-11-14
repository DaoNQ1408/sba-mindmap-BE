package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.ProcessOrderPaymentRequest;
import com.sbaproject.sbamindmap.dto.response.TransactionResponse;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.entity.Transaction;
import com.sbaproject.sbamindmap.entity.Wallet;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.enums.TransactionStatus;
import com.sbaproject.sbamindmap.enums.TransactionType;
import com.sbaproject.sbamindmap.repository.OderRepository;
import com.sbaproject.sbamindmap.repository.TransactionRepository;
import com.sbaproject.sbamindmap.repository.WalletRepository;
import com.sbaproject.sbamindmap.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final OderRepository orderRepository;

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
     * Xử lý thanh toán order từ wallet
     * BƯỚC 1: Tạo Transaction để nối Order với Wallet (status PENDING)
     * BƯỚC 2: Kiểm tra và trừ tiền từ Wallet
     * BƯỚC 3: Nếu thành công → Update Transaction status = SUCCESS và Order status = ACTIVE
     */
    @Override
    @Transactional
    public TransactionResponse processOrderPayment(ProcessOrderPaymentRequest request) {
        // 1. Validate và lấy order
        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + request.getOrderId()));

        // 2. Kiểm tra order phải ở trạng thái PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only process payment for PENDING orders. Current status: " + order.getStatus());
        }

        // 3. Kiểm tra order có package
        if (order.getPackages() == null) {
            throw new IllegalStateException("Order must have a package assigned");
        }

        // 4. Lấy wallet
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found with id: " + request.getWalletId()));

        // 5. Kiểm tra wallet có thuộc về user của order không
        if (!wallet.getUser().getId().equals(order.getUser().getId())) {
            throw new IllegalStateException("Wallet does not belong to the order's user");
        }

        // 6. Lấy giá của package
        double packagePrice = order.getPackages().getPrice();

        // 7. TẠO TRANSACTION TRƯỚC để nối Order với Wallet (status PENDING)
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setPaymentMethod(null); // Thanh toán từ wallet không cần payment method
        transaction.setType(TransactionType.PURCHASE);
        transaction.setStatus(TransactionStatus.PENDING); // Bắt đầu với PENDING
        transaction.setAmount(packagePrice);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setOrder(order);
        transactionRepository.save(transaction);

        // 8. Kiểm tra số dư đủ không
        if (wallet.getBalance() < packagePrice) {
            // Cập nhật transaction thành FAILED
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            throw new IllegalStateException(
                    String.format("Insufficient balance. Required: %.2f, Available: %.2f",
                            packagePrice, wallet.getBalance())
            );
        }

        // 9. Trừ tiền từ wallet (sau khi đã tạo transaction)
        wallet.setBalance(wallet.getBalance() - packagePrice);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // 10. Cập nhật transaction thành SUCCESS
        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);

        // 11. Cập nhật order status sang ACTIVE
        LocalDateTime now = LocalDateTime.now();
        order.setStatus(OrderStatus.ACTIVE);
        order.setActivatedAt(now);

        // 12. Set ngày hết hạn dựa trên package's durationDays (ngày hết hạn subscription)
        LocalDateTime packageExpirationDate = order.getPackages().getDurationDays();
        if (packageExpirationDate != null) {
            order.setExpiredAt(packageExpirationDate);
        } else {
            // Rollback nếu package không có expiration date
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            // Hoàn tiền
            wallet.setBalance(wallet.getBalance() + packagePrice);
            walletRepository.save(wallet);

            throw new IllegalStateException("Package must have an expiration date (durationDays)");
        }

        orderRepository.save(order);

        // 13. Return transaction response
        return convertToResponse(transaction);
    }

    /**
     * Convert Transaction entity sang TransactionResponse DTO
     */
    private TransactionResponse convertToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getWallet().getWalletId(),
                transaction.getPaymentMethod() != null ? transaction.getPaymentMethod().getName() : null,
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getOrder() != null ? transaction.getOrder().getOrderId() : null
        );
    }
}

