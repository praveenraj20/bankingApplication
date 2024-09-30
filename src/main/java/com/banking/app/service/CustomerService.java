package com.banking.app.service;

import com.banking.app.dto.CustomerDto;
import com.banking.app.entity.Customer;
import org.springframework.stereotype.Service;


public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customerDto);
}
