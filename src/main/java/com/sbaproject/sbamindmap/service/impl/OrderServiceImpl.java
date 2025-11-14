package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.OrderRequest;
import com.sbaproject.sbamindmap.entity.*;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.enums.TransactionStatus;
import com.sbaproject.sbamindmap.enums.TransactionType;
import com.sbaproject.sbamindmap.repository.*;
import com.sbaproject.sbamindmap.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OderRepository odersRepository;
    private final PackagesRepository packagesRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    /**
     * Tạo Order mới với status PENDING
     * User chọn package và tạo order, chưa thanh toán
     */
    @Override
    @Transactional
    public Orders createOrder(OrderRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));

        // Validate package exists
        Packages packages = packagesRepository.findById(request.getPackageId())
                .orElseThrow(() -> new EntityNotFoundException("Package not found with id: " + request.getPackageId()));

        // Create new order with PENDING status
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setPackages(packages);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setOrderDate(LocalDateTime.now());

        return odersRepository.save(newOrder);
    }

    /**
     * Kích hoạt Order sau khi thanh toán thành công
     * Chuyển status từ PENDING -> ACTIVE
     * - Kiểm tra số dư ví
     * - Trừ tiền từ ví
     * - Tạo transaction
     * - Set activatedAt và expiredAt = package.durationDays (ngày hết hạn của subscription)
     */
    @Override
    @Transactional
    public Orders activateOrder(long orderId) {
        Orders order = odersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Only activate if order is in PENDING status
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only activate PENDING orders. Current status: " + order.getStatus());
        }

        // Check if package exists
        if (order.getPackages() == null) {
            throw new IllegalStateException("Order must have a package assigned");
        }

        // Get user's wallet
        Wallet wallet = walletRepository.findByUser_Id(order.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found for user id: " + order.getUser().getId()));

        // Get package price
        double packagePrice = order.getPackages().getPrice();

        // Check if wallet has sufficient balance
        if (wallet.getBalance() < packagePrice) {
            throw new IllegalStateException("Insufficient balance. Required: " + packagePrice + ", Available: " + wallet.getBalance());
        }

        // Deduct amount from wallet
        wallet.setBalance(wallet.getBalance() - packagePrice);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // Get payment method (default to Wallet)
        PaymentMethod paymentMethod = paymentMethodRepository.findByName("Wallet")
                .orElseThrow(() -> new EntityNotFoundException("Payment method 'Wallet' not found"));

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setType(TransactionType.PURCHASE);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(packagePrice);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setOrder(order);
        transactionRepository.save(transaction);

        // Activate order
        LocalDateTime now = LocalDateTime.now();
        order.setStatus(OrderStatus.ACTIVE);
        order.setActivatedAt(now);

        // Set expiration date from package's durationDays (subscription expiration date)
        LocalDateTime packageExpirationDate = order.getPackages().getDurationDays();
        if (packageExpirationDate != null) {
            order.setExpiredAt(packageExpirationDate);
        } else {
            throw new IllegalStateException("Package must have an expiration date (durationDays)");
        }

        return odersRepository.save(order);
    }

    /**
     * Hủy Order
     * User không muốn thanh toán hoặc có lỗi kết nối
     * Chỉ có thể hủy order đang PENDING hoặc ACTIVE
     */
    @Override
    @Transactional
    public Orders cancelOrder(long orderId) {
        Orders order = odersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Can only cancel PENDING or ACTIVE orders
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.EXPIRED) {
            throw new IllegalStateException("Cannot cancel order with status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCompletedAt(LocalDateTime.now());

        return odersRepository.save(order);
    }

    /**
     * Cập nhật Order (admin only)
     */
    @Override
    @Transactional
    public Orders updateOrder(OrderRequest request, long orderId) {
        Orders order = odersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Update status if provided
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        // Update package if provided
        if (request.getPackageId() != null) {
            Packages packages = packagesRepository.findById(request.getPackageId())
                    .orElseThrow(() -> new EntityNotFoundException("Package not found with id: " + request.getPackageId()));
            order.setPackages(packages);
        }

        return odersRepository.save(order);
    }

    /**
     * Xóa Order
     */
    @Override
    @Transactional
    public void deleteOrder(long orderId) {
        if (!odersRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with id: " + orderId);
        }
        odersRepository.deleteById(orderId);
    }

    /**
     * Lấy Order theo ID
     */
    @Override
    public Orders getOrderById(long orderId) {
        return odersRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    /**
     * Lấy tất cả Orders
     */
    @Override
    public List<Orders> getOrders() {
        return odersRepository.findAll();
    }

    /**
     * Lấy Orders theo User ID
     */
    @Override
    public List<Orders> getOrdersByUserId(Long userId) {
        return odersRepository.findByUser_Id(userId);
    }

    /**
     * Lấy Orders theo Status
     */
    @Override
    public List<Orders> getOrdersByStatus(OrderStatus status) {
        return odersRepository.findByStatus(status);
    }

    /**
     * Scheduled task: Tự động chuyển status ACTIVE -> EXPIRED
     * Chạy mỗi giờ để kiểm tra các order đã hết hạn
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
    public void checkAndExpireOrders() {
        LocalDateTime now = LocalDateTime.now();
        List<Orders> expiredOrders = odersRepository.findExpiredActiveOrders(now);

        for (Orders order : expiredOrders) {
            order.setStatus(OrderStatus.EXPIRED);
            order.setCompletedAt(now);
            odersRepository.save(order);
        }

        if (!expiredOrders.isEmpty()) {
            System.out.println("Expired " + expiredOrders.size() + " orders at " + now);
        }
    }
}
