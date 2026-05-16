package com.example.banking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "account_balance")
@Data
public class AccountBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "curr_balance")
    private BigDecimal currBalance;

    @Column(name = "hold_balance")
    private BigDecimal holdBalance;

    @Column(name = "hold_reason")
    private String holdReason;
}