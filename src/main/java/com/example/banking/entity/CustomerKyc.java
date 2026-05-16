package com.example.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_kyc")
@Data
public class CustomerKyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "aadhar_no")
    private String aadharNo;

    @Column(name = "pan_no")
    private String panNo;
}