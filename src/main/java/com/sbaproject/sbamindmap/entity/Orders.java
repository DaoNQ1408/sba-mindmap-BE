package com.sbaproject.sbamindmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @Column(name = "price")
    private double price;
    @Column(name = "ordered status")
    private OrderStatus status;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Packages packages;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
}
