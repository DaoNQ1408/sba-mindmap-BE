package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.OrderRequest;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    Orders createOrder(OrderRequest ord);
    Orders updateOrder(OrderRequest ord, long orderId);
    Orders activateOrder(long orderId);
    Orders cancelOrder(long orderId);
    void deleteOrder(long orderId);
    Orders getOrderById(long orderId);
    List<Orders> getOrders();
    List<Orders> getOrdersByUserId(Long userId);
    List<Orders> getOrdersByStatus(OrderStatus status);
    void checkAndExpireOrders();
}
