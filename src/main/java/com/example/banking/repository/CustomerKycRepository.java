package com.example.banking.repository;

import com.example.banking.entity.CustomerKyc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerKycRepository extends JpaRepository<CustomerKyc, Long> {
}