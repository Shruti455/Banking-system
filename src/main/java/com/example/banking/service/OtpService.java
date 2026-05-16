package com.example.banking.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    // mobile -> otp
    private final Map<String, String> otpStorage = new HashMap<>();

    // SEND OTP
    public String sendOtp(String mobileNumber) {

        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);

        String generatedOtp = String.valueOf(otp);

        otpStorage.put(mobileNumber, generatedOtp);

        System.out.println("OTP : " + generatedOtp);

        return generatedOtp;
    }

    // VERIFY OTP
    public boolean verifyOtp(String mobileNumber, String enteredOtp) {

        String storedOtp = otpStorage.get(mobileNumber);

        if (storedOtp == null) {
            return false;
        }

        boolean matched = storedOtp.equals(enteredOtp);

        if (matched) {
            otpStorage.remove(mobileNumber);
        }

        return matched;
    }
}