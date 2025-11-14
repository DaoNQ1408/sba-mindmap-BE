package com.sbaproject.sbamindmap.service.admin;

import com.sbaproject.sbamindmap.entity.Transaction;

import java.util.List;

public interface AdminTransactionService {
    List<Transaction> getAll();
    Transaction getById(Long id);
    void delete(Long id);
}