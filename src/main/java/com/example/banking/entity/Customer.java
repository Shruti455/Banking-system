package com.example.banking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String firstName;
    private String lastName;

    private LocalDate dob;

    private String gender;
    private String motherName;
    private String fatherName;
    private String maritalStatus;
    private String occupation;
    private String citizenship;

    private String photograph;

    private String status = "STEP1";
}