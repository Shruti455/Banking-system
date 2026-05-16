package com.example.banking.dto;

import lombok.Data;

@Data
public class CustomerStep3DTO {

    private String aadhaarNumber;
    private String panNumber;

    private String addressLine1;
    private String city;
    private String state;
    private String postalCode;
}