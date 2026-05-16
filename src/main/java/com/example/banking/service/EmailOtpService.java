package com.example.banking.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailOtpService {

    private final EmailService emailService;

    private final Map<String, String> otpStorage =
            new HashMap<>();

    public EmailOtpService(EmailService emailService) {
        this.emailService = emailService;
    }

    // SEND OTP
    public String sendOtp(String email) throws Exception {

        String otp =String.valueOf( 100000 + new Random().nextInt(900000) );

        otpStorage.put(email, otp);

        emailService.sendOtpEmail(email, otp);

        return "OTP SENT";
    }

    // VERIFY OTP
    public boolean verifyOtp(String email, String otp) {

        String storedOtp = otpStorage.get(email);

        return storedOtp != null && storedOtp.equals(otp);
    }
}