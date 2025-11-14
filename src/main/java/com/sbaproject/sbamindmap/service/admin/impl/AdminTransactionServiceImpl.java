package com.sbaproject.sbamindmap.service.admin.impl;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.entity.Transaction;
import com.sbaproject.sbamindmap.repository.TransactionRepository;
import com.sbaproject.sbamindmap.service.admin.AdminTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTransactionServiceImpl implements AdminTransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
}