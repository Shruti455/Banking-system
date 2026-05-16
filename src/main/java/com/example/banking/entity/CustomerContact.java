package com.example.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_contact")
@Data
public class CustomerContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // MANY CONTACTS -> ONE CUSTOMER
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // PHONE / EMAIL
    private String type;

    // actual value
    private String contact;

    private LocalDateTime verifiedAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}