package com.sbaproject.sbamindmap.controller;

import com.sbaproject.sbamindmap.dto.request.OrderRequest;
import com.sbaproject.sbamindmap.dto.response.ResponseBase;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<ResponseBase> createOrder(@RequestBody OrderRequest order) {
        Orders orders = orderService.creatOrder(order);
        if (orders != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Order created successfully", orders));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to create order", null));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBase> updateOrder(@PathVariable long id, @RequestBody OrderRequest order) {
        Orders updatedOrder = orderService.updateOrder(order, id);
        if (updatedOrder != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Order updated successfully", updatedOrder));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to update order", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBase> deleteOrder(@PathVariable long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(new ResponseBase(200, "Delete Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseBase(500, "Failed to delete", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBase> getOrderById(@PathVariable long id) {
        Orders order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Order retrieved successfully", order));
        } else {
            return ResponseEntity.status(404).body(new ResponseBase(404, "Order not found", null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseBase> getAllOrders() {
        return ResponseEntity.ok(new ResponseBase(200, "Orders retrieved successfully", orderService.getOrders()));
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<ResponseBase> completeOrder(@PathVariable long id) {
        Orders completedOrder = orderService.updateOrderComplete(id);
        if (completedOrder != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Order completed successfully", completedOrder));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to complete order", null));
        }
    }

    @PostMapping("/{orderId}/packages/{packageId}")
    public ResponseEntity<ResponseBase> setRelatedPackage(@PathVariable long orderId, @PathVariable long packageId) {
        Orders updatedOrder = orderService.setRelatedPackage(orderId, packageId);
        if (updatedOrder != null) {
            return ResponseEntity.ok(new ResponseBase(200, "Related package set successfully", updatedOrder));
        } else {
            return ResponseEntity.status(500).body(new ResponseBase(409, "Failed to set related package", null));
        }
    }
}
