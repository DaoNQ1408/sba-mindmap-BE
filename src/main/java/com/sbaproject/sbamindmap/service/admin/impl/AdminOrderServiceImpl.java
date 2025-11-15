package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.dto.request.AdminOrderUpdateStatusRequestDto;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.repository.OderRepository;
import com.sbaproject.sbamindmap.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OderRepository orderRepository;

    @Override
    public List<Orders> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Orders getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Orders> getByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Orders updateStatus(Long id, AdminOrderUpdateStatusRequestDto request) {
        Orders order = getById(id);
        OrderStatus newStatus = request.getStatus();

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.ACTIVE) {
            order.setActivatedAt(LocalDateTime.now());
        }

        if (newStatus == OrderStatus.EXPIRED) {
            order.setExpiredAt(LocalDateTime.now());
        }

        if (newStatus == OrderStatus.COMPLETED) {
            order.setCompletedAt(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        Orders order = getById(id);
        orderRepository.delete(order);
    }
}

