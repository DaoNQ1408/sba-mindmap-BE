package com.sbaproject.sbamindmap.service.impl;

import com.sbaproject.sbamindmap.dto.request.OrderRequest;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.entity.Packages;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.exception.DuplicateObjectException;
import com.sbaproject.sbamindmap.repository.OdersRepository;
import com.sbaproject.sbamindmap.repository.PackagesRepository;
import com.sbaproject.sbamindmap.service.OdersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OdersService {
    @Autowired
    private OdersRepository odersRepository;

    @Autowired
    private PackagesRepository packagesRepository;

    @Override
    @Transactional
    public Orders creatOrder(OrderRequest ord) {
        Orders newOrder = new Orders();
        Orders exitedOrder = odersRepository.findById(newOrder.getOrderId()).orElse(null);
        if (exitedOrder == null) {
            newOrder.setStatus(newOrder.getStatus());
            newOrder.setOrderDate(LocalDateTime.now());
            return odersRepository.save(newOrder);
        }
        return null;
    }

    @Override
    public Orders setRelatedPackage(long orderId, long packageId) {
        Packages relatedPackage = packagesRepository.findById(packageId).orElseThrow(() -> new EntityNotFoundException("Package not found"));
        Orders exitedOrder = odersRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order is not exited"));

        if (exitedOrder != null && relatedPackage != null) {
            exitedOrder.setPackages(relatedPackage);
            return odersRepository.save(exitedOrder);
        }
        return null;
    }

    @Override
    @Transactional
    public Orders updateOrderComplete(long orderId) {
        Orders exitedOrder = odersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order is not exited"));
        exitedOrder.setStatus(OrderStatus.COMPLETED);
        exitedOrder.setCompletedAt(LocalDateTime.now());
        return odersRepository.save(exitedOrder);
    }

    @Override
    @Transactional
    public Orders updateOrder(OrderRequest ord, long orderId) {
        Orders exitedOrder = odersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order is not exited"));
        if (exitedOrder != null) {
            exitedOrder.setStatus(ord.getStatus());
            exitedOrder.setOrderDate(LocalDateTime.now());
            return odersRepository.save(exitedOrder);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteOrder(long orderId) {
        odersRepository.deleteById(orderId);
    }

    @Override
    public Orders getOrderById(long orderId) {
        return odersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order is not exited"));
    }

    @Override
    public List<Orders> getOrders() {
        return odersRepository.findAll();
    }
}
