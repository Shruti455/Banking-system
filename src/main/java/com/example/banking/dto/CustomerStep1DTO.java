package com.example.banking.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerStep1DTO {

    private String title;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String gender;
    private String motherName;
    private String fatherName;
    private String maritalStatus;
    private String occupation;
    private String citizenship;
    private MultipartFile photograph; 
}