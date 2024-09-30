package com.banking.app.service;

import com.banking.app.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionServiceImpl implements TransactionService{
    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return null;
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {
        return null;
    }
}
