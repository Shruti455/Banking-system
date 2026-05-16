package com.example.banking.repository;

import com.example.banking.entity.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    CustomerAccount findByCustomerId(Long customerId);
}