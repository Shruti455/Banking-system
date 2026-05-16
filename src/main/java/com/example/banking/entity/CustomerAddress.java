package com.example.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_address")
@Data
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    private String type;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    private String city;

    private String state;

    private String pincode;
}