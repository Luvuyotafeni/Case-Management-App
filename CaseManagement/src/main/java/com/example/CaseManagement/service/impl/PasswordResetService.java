package com.example.CaseManagement.service.impl;


import com.example.CaseManagement.entity.PasswordResetTokenEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.repository.CredentialsRepository;
import com.example.CaseManagement.repository.PasswordResetTokenRepository;
import com.example.CaseManagement.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CredentialsRepository credentialsRepository;

    //Generating a random 6-digit otp
    private String generateOtp(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    @Transactional
    public void requestPasswordReset(String email){
        try {
            //checking if user exists
            UserBaseEntity user = userService.getUserByEmail(email);

            String otp = generateOtp();

            PasswordResetTokenEntity token = new PasswordResetTokenEntity();
            token.setEmail(email);
            token.setOtp(otp);
            token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
            tokenRepository.save(token);

            emailService.sendResetOtpEmail(email, otp);
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public boolean validateOtp(String email, String otp){
        Optional<PasswordResetTokenEntity> tokenOtp = tokenRepository.findByEmailAndOtpAndUsedFalse(email, otp);

        if (tokenOtp.isPresent()){
            PasswordResetTokenEntity token = tokenOtp.get();

            if (token.isExpired()){
                return false;
            }

            return true;
        }

        return false;
    }

    @Transactional
    public void resetPassword(String email, String otp, String newPassword){
        Optional<PasswordResetTokenEntity> tokenOtp = tokenRepository.findByEmailAndOtpAndUsedFalse(email, otp);

        if (tokenOtp.isEmpty() || tokenOtp.get().isExpired()){
            throw new RuntimeException("Invalid or expired otp");
        }

        PasswordResetTokenEntity token = tokenOtp.get();
        UserBaseEntity user = userService.getUserByEmail(email);

        // Update this line to use the correct method name
        var credentials = credentialsRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(()-> new RuntimeException("credentials not found"));

        credentials.setPassword(passwordEncoder.encode(newPassword));
        credentialsRepository.save(credentials);

        token.setUsed(true);
        tokenRepository.save(token);
    }
}
