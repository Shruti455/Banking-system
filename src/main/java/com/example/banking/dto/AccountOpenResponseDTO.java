package com.example.banking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountOpenResponseDTO {

    private String customerName;

    private String accountNumber;

    private String crn;

    private String accountType;

    private BigDecimal balance;

    private String status;
}