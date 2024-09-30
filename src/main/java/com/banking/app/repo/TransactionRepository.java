package com.banking.app.repo;

import com.banking.app.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
        List<Transaction> findByAccountId(Long accountId);
}