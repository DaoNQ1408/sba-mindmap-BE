package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.CommunityStatus;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private OrderStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime orderDate;
}
