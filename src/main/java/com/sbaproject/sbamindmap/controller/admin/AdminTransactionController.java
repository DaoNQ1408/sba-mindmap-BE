package com.sbaproject.sbamindmap.controller.admin;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Transaction;
import com.sbaproject.sbamindmap.service.admin.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
public class AdminTransactionController {

    private final AdminTransactionService adminTransactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(adminTransactionService.getAll(), "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminTransactionService.getById(id), "Success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminTransactionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Transaction deleted successfully"));
    }
}