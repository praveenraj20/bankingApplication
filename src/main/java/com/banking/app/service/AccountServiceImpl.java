package com.banking.app.service;

import com.banking.app.entity.Account;
import com.banking.app.entity.Transaction;
import com.banking.app.repo.AccountRepository;
import com.banking.app.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.005"); // 0.5%
    private static final BigDecimal TRANSACTION_FEE_RATE = new BigDecimal("0.0005"); // 0.05%

    @Transactional
    public String transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid from account ID"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid to account ID"));

        // Calculate transaction fee
        BigDecimal transactionFee = amount.multiply(TRANSACTION_FEE_RATE);
        BigDecimal totalDeduction = amount.add(transactionFee);

        if (fromAccount.getBalance().compareTo(totalDeduction) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Deduct amount and fee from the source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(totalDeduction));
        accountRepository.save(fromAccount);

        // Add amount to the destination account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Create transaction records
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(totalDeduction);
        debitTransaction.setTimestamp(LocalDateTime.now());
        debitTransaction.setType("TRANSFER_OUT");
        debitTransaction.setAccount(fromAccount);
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount);
        creditTransaction.setTimestamp(LocalDateTime.now());
        creditTransaction.setType("TRANSFER_IN");
        creditTransaction.setAccount(toAccount);
        transactionRepository.save(creditTransaction);

        // Credit interest to the savings account (if applicable)
        if (fromAccount.getAccType().equals("SAVINGS")) {
            BigDecimal interest = fromAccount.getBalance().multiply(INTEREST_RATE);
            fromAccount.setBalance(fromAccount.getBalance().add(interest));
            accountRepository.save(fromAccount);

            // Create interest transaction record
            Transaction interestTransaction = new Transaction();
            interestTransaction.setAmount(interest);
            interestTransaction.setTimestamp(LocalDateTime.now());
            interestTransaction.setType("INTEREST_CREDIT");
            interestTransaction.setAccount(fromAccount);
            transactionRepository.save(interestTransaction);
        }

        return "Transaction completed successfully.";
    }
}
