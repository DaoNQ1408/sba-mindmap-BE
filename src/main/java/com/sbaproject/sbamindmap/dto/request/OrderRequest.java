package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.entity.OrderStatus;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class OrderRequest {
    private double price;
    private OrderStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime orderDate;

}
