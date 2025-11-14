package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long userId;
    private Long packageId;
    private OrderStatus status; // Optional, for admin updates
}
