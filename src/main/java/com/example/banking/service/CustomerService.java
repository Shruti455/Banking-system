package com.example.banking.service;

import com.example.banking.dto.CustomerStep1DTO;
import com.example.banking.dto.CustomerStep3DTO;
import com.example.banking.dto.CustomerStep4DTO;
import com.example.banking.entity.Customer;
import com.example.banking.entity.CustomerAccount;
import com.example.banking.entity.CustomerAddress;
import com.example.banking.entity.CustomerKyc;
import com.example.banking.entity.Nominee;
import com.example.banking.repository.*;

import com.example.banking.entity.AccountBalance;
import com.example.banking.dto.AccountOpenResponseDTO;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerKycRepository kycRepo;
    private final CustomerAddressRepository addressRepo;
    private final CustomerAccountRepository accountRepo;
    private final NomineeRepository nomineeRepo;
    private final AccountBalanceRepository balanceRepo;

    public CustomerService(CustomerRepository repo, CustomerKycRepository kycRepo, CustomerAddressRepository addressRepo, CustomerAccountRepository accountRepo, NomineeRepository nomineeRepo, AccountBalanceRepository balanceRepo) {
        this.repo = repo;
        this.kycRepo = kycRepo;
        this.addressRepo = addressRepo;
        this.accountRepo = accountRepo;
        this.nomineeRepo = nomineeRepo;
        this.balanceRepo = balanceRepo;
    }

     // =========================
    // STEP 1
    // =========================

    public Customer saveStep1(CustomerStep1DTO dto) {

    System.out.println("DOB: " + dto.getDob());
    System.out.println("FILE: " + dto.getPhotograph());

    Customer c = new Customer();

    c.setTitle(dto.getTitle());
    c.setFirstName(dto.getFirstName());
    c.setLastName(dto.getLastName());

    // DOB
    c.setDob(dto.getDob());

    c.setGender(dto.getGender());
    c.setMotherName(dto.getMotherName());
    c.setFatherName(dto.getFatherName());
    c.setMaritalStatus(dto.getMaritalStatus());
    c.setOccupation(dto.getOccupation());
    c.setCitizenship(dto.getCitizenship());

    MultipartFile file = dto.getPhotograph();

        if (file != null && !file.isEmpty()) {

            try {

                String uploadDir = "uploads/";

                String originalName = 
                    file.getOriginalFilename() == null
                    ? "image.jpg"
                    : file.getOriginalFilename().replaceAll("\\s+", "_");

                String fileName =
                        UUID.randomUUID() + "_" + originalName;

                Path uploadPath = Paths.get(uploadDir);

                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);

                Files.write(filePath, file.getBytes());

                c.setPhotograph(fileName);

            } catch (Exception e) {

                e.printStackTrace();

                throw new RuntimeException("Image upload failed");
            }
        }

        return repo.save(c);
    }

    // =========================
    // STEP 3
    // =========================

    public void saveStep3(Long customerId, CustomerStep3DTO dto) {

        // SAVE KYC
        CustomerKyc kyc = new CustomerKyc();

        kyc.setCustomerId(customerId);
        kyc.setAadharNo(dto.getAadhaarNumber());
        kyc.setPanNo(dto.getPanNumber());

        kycRepo.save(kyc);

        // SAVE ADDRESS
        CustomerAddress address = new CustomerAddress();

        address.setCustomerId(customerId);
        address.setType("permanent");
        address.setAddress1(dto.getAddressLine1());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPostalCode());

        addressRepo.save(address);
    }

     // =========================
    // STEP 4
    // =========================

    public void saveStep4(Long customerId, CustomerStep4DTO dto) {

    // SAVE ACCOUNT

    CustomerAccount account = new CustomerAccount();

    account.setCustomerId(customerId);

    account.setType(
            dto.getAccountType() == null ||
            dto.getAccountType().isEmpty()
                    ? "Savings"
                    : dto.getAccountType()
    );

    account.setCrn(generateCRN());

    account.setAccNumber(generateAccountNumber());

    account.setAccStatus("active");

    accountRepo.save(account);

    // SAVE NOMINEE

        if ("Yes".equalsIgnoreCase(dto.getNomineeOpted())) {

            Nominee nominee = new Nominee();

            nominee.setCustomerId(customerId);

            nominee.setNomineeName(dto.getNomineeName());

            nominee.setRelation(dto.getNomineeRelation());

            if (dto.getNomineeDob() != null && !dto.getNomineeDob().isEmpty()) {
                try {
                    nominee.setNomineeDob(LocalDate.parse(dto.getNomineeDob()));
                } catch (Exception e) {
                    throw new RuntimeException("Invalid Nominee DOB: " + dto.getNomineeDob());
                }
            }

            nomineeRepo.save(nominee);
        }
    }

    private String generateCRN() {

        long random =
                (long)(Math.random() * 90000000L) + 10000000L;

        return "CRN" + random;
    }

    private String generateAccountNumber() {

        String prefix = "2026";

        long random =
                (long)(Math.random() * 100000000L);

        return prefix + String.format("%08d", random);
    }
    
    // =========================
    // SUBMIT APPLICATION

    public AccountOpenResponseDTO submitApplication(Long customerId) {

        Customer customer = repo.findById(customerId)
                .orElseThrow();

        // CUSTOMER STATUS UPDATE
        customer.setStatus("ACTIVE");

        repo.save(customer);

        // ACCOUNT FIND
        CustomerAccount account =
                accountRepo.findByCustomerId(customerId);

        // BALANCE SAVE
        AccountBalance balance = new AccountBalance();

        balance.setAccountId(account.getId());

        balance.setCurrBalance(BigDecimal.ZERO);

        balanceRepo.save(balance);

        // RESPONSE DTO
        AccountOpenResponseDTO dto =
                new AccountOpenResponseDTO();

        dto.setCustomerName(
                customer.getFirstName() + " " +
                customer.getLastName()
        );

        dto.setAccountNumber(
                account.getAccNumber()
        );

        dto.setCrn(
                account.getCrn()
        );

        dto.setAccountType(
                account.getType()
        );

        dto.setBalance(
                balance.getCurrBalance()
        );

        dto.setStatus(
                account.getAccStatus()
        );

        return dto;
    }
    

    
}