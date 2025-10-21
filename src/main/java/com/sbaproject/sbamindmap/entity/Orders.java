package com.sbaproject.sbamindmap.entity;

import com.sbaproject.sbamindmap.enums.CommunityStatus;
import com.sbaproject.sbamindmap.enums.OrderStatus;
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
    @Column(name = "ordered status")
    private OrderStatus status = OrderStatus.PENDING;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Packages packages;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
}
