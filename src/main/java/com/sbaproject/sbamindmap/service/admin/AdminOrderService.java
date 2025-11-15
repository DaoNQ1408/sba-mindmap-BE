package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.dto.request.AdminOrderUpdateStatusRequestDto;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.enums.OrderStatus;

import java.util.List;

public interface AdminOrderService {
    List<Orders> getAll();
    Orders getById(Long id);
    List<Orders> getByStatus(OrderStatus status);
    Orders updateStatus(Long id, AdminOrderUpdateStatusRequestDto request);
    void delete(Long id);
}

