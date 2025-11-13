package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.OrderRequest;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.service.OdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OdersService odersService;

    /**
     * Tạo Order mới với status PENDING
     * Request body: { "userId": 1, "packageId": 1 }
     */
    @PostMapping("/")
    public ResponseEntity<ResponseBase> createOrder(@RequestBody OrderRequest order) {
        try {
            Orders orders = odersService.createOrder(order);
            return ResponseEntity.ok(new ResponseBase(200, "Order created successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to create order: " + e.getMessage(), null));
        }
    }

    /**
     * Cập nhật Order (admin only)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseBase> updateOrder(@PathVariable long id, @RequestBody OrderRequest order) {
        try {
            Orders updatedOrder = odersService.updateOrder(order, id);
            return ResponseEntity.ok(new ResponseBase(200, "Order updated successfully", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to update order: " + e.getMessage(), null));
        }
    }

    /**
     * Kích hoạt Order sau khi thanh toán thành công
     * Chuyển status PENDING -> ACTIVE
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<ResponseBase> activateOrder(@PathVariable long id) {
        try {
            Orders activatedOrder = odersService.activateOrder(id);
            return ResponseEntity.ok(new ResponseBase(200, "Order activated successfully", activatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to activate order: " + e.getMessage(), null));
        }
    }

    /**
     * Hủy Order
     * User không muốn thanh toán hoặc có lỗi
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ResponseBase> cancelOrder(@PathVariable long id) {
        try {
            Orders cancelledOrder = odersService.cancelOrder(id);
            return ResponseEntity.ok(new ResponseBase(200, "Order cancelled successfully", cancelledOrder));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to cancel order: " + e.getMessage(), null));
        }
    }

    /**
     * Xóa Order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBase> deleteOrder(@PathVariable long id) {
        try {
            odersService.deleteOrder(id);
            return ResponseEntity.ok(new ResponseBase(200, "Order deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to delete order: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy Order theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase> getOrderById(@PathVariable long id) {
        try {
            Orders order = odersService.getOrderById(id);
            return ResponseEntity.ok(new ResponseBase(200, "Order retrieved successfully", order));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ResponseBase(404, "Order not found: " + e.getMessage(), null));
        }
    }

    /**
     * Lấy tất cả Orders
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseBase> getAllOrders() {
        return ResponseEntity.ok(new ResponseBase(200, "Orders retrieved successfully", odersService.getOrders()));
    }

    /**
     * Lấy Orders theo User ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseBase> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(new ResponseBase(200, "User orders retrieved successfully", odersService.getOrdersByUserId(userId)));
    }

    /**
     * Lấy Orders theo Status
     * Example: /api/orders/status/PENDING
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseBase> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(new ResponseBase(200, "Orders retrieved successfully", odersService.getOrdersByStatus(status)));
    }

    /**
     * Trigger manual check for expired orders (admin only)
     */
    @PostMapping("/check-expired")
    public ResponseEntity<ResponseBase> checkExpiredOrders() {
        try {
            odersService.checkAndExpireOrders();
            return ResponseEntity.ok(new ResponseBase(200, "Expired orders checked successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to check expired orders: " + e.getMessage(), null));
        }
    }
}
