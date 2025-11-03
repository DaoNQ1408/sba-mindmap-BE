package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sbaproject.sbamindmap.entity.Orders;
import com.sbaproject.sbamindmap.repository.OrdersRepository;
import java.util.*;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
public class AdminTransactionController {

    private final OrdersRepository ordersRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Orders>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(ordersRepository.findAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> getById(@PathVariable Long id) {
        return ordersRepository.findById(id)
                .map(order -> ResponseEntity.ok(ApiResponse.success(order, "Success")))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error(null, "Transaction not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Orders>> delete(@PathVariable Long id) {
        if (!ordersRepository.existsById(id)) {
            return ResponseEntity.status(404).body(ApiResponse.error(null, "Transaction not found"));
        }
        ordersRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Transaction deleted successfully"));
    }
}
