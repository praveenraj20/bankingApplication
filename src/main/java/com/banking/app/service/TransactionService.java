package com.banking.app.service;

import com.banking.app.entity.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactionsByAccountId(Long accountId);

    Transaction getTransactionById(Long transactionId);
}
