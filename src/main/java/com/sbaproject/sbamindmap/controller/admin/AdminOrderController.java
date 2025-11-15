package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.request.AdminOrderUpdateStatusRequestDto;
import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.enums.OrderStatus;
import com.sbaproject.sbamindmap.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Orders>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminOrderService.getAll(), "OK"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminOrderService.getById(id), "OK"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Orders>>> getByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(ApiResponse.success(adminOrderService.getByStatus(status), "OK"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Orders>> updateStatus(
            @PathVariable Long id,
            @RequestBody AdminOrderUpdateStatusRequestDto request
    ) {
        Orders updated = adminOrderService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Status updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminOrderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Order deleted"));
    }
}

