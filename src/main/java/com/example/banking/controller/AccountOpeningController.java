package com.example.banking.controller;

import com.example.banking.dto.CustomerStep1DTO;
import com.example.banking.dto.CustomerStep3DTO;
import com.example.banking.dto.CustomerStep4DTO;
import com.example.banking.entity.Customer;
import com.example.banking.entity.CustomerContact;
import com.example.banking.repository.CustomerContactRepository;
import com.example.banking.repository.CustomerRepository;
import com.example.banking.service.CustomerService;
import com.example.banking.service.EmailOtpService;
import com.example.banking.service.OtpService;
import com.example.banking.service.AadhaarOtpService;
import com.example.banking.dto.AccountOpenResponseDTO;

import java.nio.file.Files;
import java.nio.file.Paths;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Path;

@Controller
public class AccountOpeningController {

    private final CustomerService service;

    private final OtpService otpService;
    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final EmailOtpService emailOtpService;
    private final AadhaarOtpService aadhaarOtpService;

    public AccountOpeningController(CustomerService service, OtpService otpService, CustomerRepository customerRepository, CustomerContactRepository customerContactRepository, EmailOtpService emailOtpService, AadhaarOtpService aadhaarOtpService) {
        this.service = service;
        this.otpService = otpService;
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.emailOtpService = emailOtpService;
        this.aadhaarOtpService = aadhaarOtpService;
    }

    // =========================================
    // ACCOUNT OPENING PAGE
    // =========================================

    @GetMapping("/account-opening")
    public String accountOpening() {

        return "account_opening";
    }

    // =========================================
    // SUCCESS PAGE
    // =========================================

    @GetMapping("/account-opening/success")
    public String successPage() {

        return "account_success";
    }

    // =========================================
    // STEP 1 SAVE
    // =========================================

    @PostMapping("/account-opening/step1")
    @ResponseBody
    public String saveStep1( @ModelAttribute CustomerStep1DTO dto) 
    {
        
        Customer saved = service.saveStep1(dto);

        // return customer id
        return saved.getId().toString();
    }

    // =========================================
    // GET UPLOADED IMAGE

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws Exception {
        Path filePath = Paths.get("uploads").resolve(filename).normalize();

        byte[] image = Files.readAllBytes(filePath);

        return ResponseEntity
                .ok()
                .header("Content-Type", "image/jpeg")
                .body(image);
    }

    // =========================================
    // STEP 3 SAVE
    // =========================================

    @PostMapping("/account-opening/step3/{id}")
    @ResponseBody
    public String saveStep3(

            @PathVariable Long id,

            @ModelAttribute CustomerStep3DTO dto
    ) {

        service.saveStep3(id, dto);

        return "STEP3 SAVED";
    }

    // =========================================
    // STEP 4 SAVE
    // =========================================

    @PostMapping("/account-opening/step4/{customerId}")
    @ResponseBody
    public String saveStep4(@PathVariable Long customerId,@ModelAttribute CustomerStep4DTO dto) 
    {

        service.saveStep4(customerId, dto);

        return "STEP4 SAVED";
    }

    // =========================================
    // SEND OTP
    @PostMapping("/account-opening/send-mobile-otp")
    @ResponseBody
    public String sendMobileOtp(@RequestParam String mobileNumber) {

        boolean alreadyExists =
                customerContactRepository
                        .existsByContact(mobileNumber);

        if (alreadyExists) {

            return "MOBILE ALREADY EXISTS";
        }

        return otpService.sendOtp(mobileNumber);
    }

    // =========================================
    // VERIFY OTP

    @PostMapping("/account-opening/verify-mobile-otp")
    @ResponseBody
    public String verifyMobileOtp(
            @RequestParam Long customerId,
            @RequestParam String mobileNumber,
            @RequestParam String otp
    ) {

        boolean verified =
                otpService.verifyOtp(mobileNumber, otp);

        if (!verified) {

            return "INVALID OTP";
        }

        // =========================
        // MOBILE ALREADY EXISTS
        // =========================

        boolean alreadyExists =
                customerContactRepository
                        .existsByContact(mobileNumber);

        if (alreadyExists) {

            return "MOBILE ALREADY EXISTS";
        }

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow();

        CustomerContact contact =
                new CustomerContact();

        contact.setCustomer(customer);

        contact.setType("PHONE");

        contact.setContact(mobileNumber);

        contact.setVerifiedAt(
                java.time.LocalDateTime.now());

        customerContactRepository.save(contact);

        return "MOBILE VERIFIED";
    }

    // =========================================
    // SEND EMAIL OTP

    @PostMapping("/account-opening/send-email-otp")
    @ResponseBody
    public String sendEmailOtp(@RequestParam String email)
            throws Exception {

        boolean alreadyExists =
                customerContactRepository
                        .existsByContact(email);

        if (alreadyExists) {

            return "EMAIL ALREADY EXISTS";
        }

        return emailOtpService.sendOtp(email);
    }

    // =========================================
    // VERIFY EMAIL OTP

    @PostMapping("/account-opening/verify-email-otp")
    @ResponseBody
    public String verifyEmailOtp(
            @RequestParam Long customerId,
            @RequestParam String email,
            @RequestParam String otp)
    {

        boolean verified =
                emailOtpService.verifyOtp(email, otp);

        if (!verified) {
            return "INVALID OTP";
        }

        boolean alreadyExists =
        customerContactRepository
                .existsByContact(email);

        if (alreadyExists) {

            return "EMAIL ALREADY EXISTS";
        }

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow();

        CustomerContact contact =
                new CustomerContact();

        contact.setCustomer(customer);

        contact.setType("EMAIL");

        contact.setContact(email);

        contact.setVerifiedAt(
                java.time.LocalDateTime.now());

        customerContactRepository.save(contact);

        return "EMAIL VERIFIED";
    }

    // =========================================
    // SEND AADHAAR OTP
    // =========================================

    @PostMapping("/account-opening/send-aadhaar-otp")
    @ResponseBody
    public String sendAadhaarOtp(
            @RequestParam String aadhaarNumber
    ) {

        return aadhaarOtpService
                .sendOtp(aadhaarNumber);
    }


    // =========================================
    // VERIFY AADHAAR OTP
    // =========================================

    @PostMapping("/account-opening/verify-aadhaar-otp")
    @ResponseBody
    public String verifyAadhaarOtp(

            @RequestParam String aadhaarNumber,

            @RequestParam String otp

    ) {

        boolean verified =
                aadhaarOtpService.verifyOtp(
                        aadhaarNumber,
                        otp
                );

        if (!verified) {

            return "INVALID OTP";
        }

        return "AADHAAR VERIFIED";
    }

    // =========================================
    // SUBMIT APPLICATION
    @PostMapping("/account-opening/submit/{customerId}")
    @ResponseBody
    public AccountOpenResponseDTO submitApplication(
            @PathVariable Long customerId
    ) {

        AccountOpenResponseDTO result =
                service.submitApplication(customerId);

        System.out.println(result);

        return result;
    }

    // =========================================
    // LOGIN PAGE
    // =========================================

        @GetMapping("/login")
        public String loginPage() {

        return "login";
        }

    
}