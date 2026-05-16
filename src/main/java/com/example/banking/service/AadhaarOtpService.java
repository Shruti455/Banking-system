package com.example.banking.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AadhaarOtpService {

    private final Map<String, String> otpStorage =
            new HashMap<>();

    // SEND OTP
    public String sendOtp(String aadhaarNumber) {

        String otp =
                String.valueOf(
                        100000 + new Random().nextInt(900000)
                );

        otpStorage.put(aadhaarNumber, otp);

        System.out.println("AADHAAR OTP : " + otp);

        return otp;
    }

    // VERIFY OTP
    public boolean verifyOtp(
            String aadhaarNumber,
            String otp
    ) {

        String storedOtp =
                otpStorage.get(aadhaarNumber);

        return storedOtp != null &&
                storedOtp.equals(otp);
    }
}