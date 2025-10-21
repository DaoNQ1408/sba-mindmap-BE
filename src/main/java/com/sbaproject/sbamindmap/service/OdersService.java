package com.sbaproject.sbamindmap.service;

import com.sbaproject.sbamindmap.dto.request.OrderRequest;
import com.sbaproject.sbamindmap.entity.Orders;

import java.util.List;

public interface OdersService {
    public Orders creatOrder(OrderRequest ord);
    public Orders updateOrder(OrderRequest ord, long orderId);
    public Orders updateOrderComplete( long orderId);
    public void deleteOrder(long orderId);
    public Orders getOrderById(long orderId);
    public List<Orders> getOrders();
    public Orders setRelatedPackage(long orderId, long packageId);
}
