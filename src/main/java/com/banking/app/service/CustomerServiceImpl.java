package com.banking.app.service;

import com.banking.app.dto.CustomerDto;
import com.banking.app.entity.Account;
import com.banking.app.entity.Customer;
import com.banking.app.repo.AccountRepository;
import com.banking.app.repo.CustomerRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);

        // Create Current and Savings accounts
        Account currentAccount = new Account();
        currentAccount.setAccType("Current");
        currentAccount.setBalance(BigDecimal.ZERO);
        currentAccount.setAccountNumber(generateRandomAccountNumber());
        currentAccount.setCustomer(customer);

        Account savingsAccount = new Account();
        savingsAccount.setAccType("Savings");
        savingsAccount.setBalance(new BigDecimal("500.00")); // Joining bonus
        savingsAccount.setAccountNumber(generateRandomAccountNumber());
        savingsAccount.setCustomer(customer);

        // Add accounts to the customer
        customer.setAccounts(Arrays.asList(currentAccount, savingsAccount));

        // Save customer (cascades to accounts)
        customer = customerRepository.save(customer);

        return modelMapper.map(customer, CustomerDto.class);
    }

    // Generates a numeric random account number (12 digits)
    private String generateRandomAccountNumber() {
        return String.valueOf((long)(Math.random() * 1_000_000_000_000L));
    }
}

