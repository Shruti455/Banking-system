package com.example.banking.repository;

import com.example.banking.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long> {
    boolean existsByContact(String contact);

}