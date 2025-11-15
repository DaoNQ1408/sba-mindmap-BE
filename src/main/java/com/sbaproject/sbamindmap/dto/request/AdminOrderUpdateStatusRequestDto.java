package com.sbaproject.sbamindmap.dto.request;

import com.sbaproject.sbamindmap.enums.OrderStatus;
import lombok.Data;

@Data
public class AdminOrderUpdateStatusRequestDto {
    private OrderStatus status;
}

