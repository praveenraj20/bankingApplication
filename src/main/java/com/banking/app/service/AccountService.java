package com.banking.app.service;

import java.math.BigDecimal;

public interface AccountService {
    String transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
