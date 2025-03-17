package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.EmailVerificationTokenEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.repository.EmailVerificationRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private EmailService emailService;

    private String generateVerificationCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Transactional
    public void sendVerificationEmail(UserBaseEntity user){
        //deleting the existing verification codes
        emailVerificationRepository.deleteByEmail(user.getEmail());

        //Generating new verificationn code
        String verificationCode = generateVerificationCode();

        //Creating the verification Token with 10 minutes
        EmailVerificationTokenEntity token = new EmailVerificationTokenEntity();
        token.setEmail(user.getEmail());
        token.setVerificationCode(verificationCode);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        emailVerificationRepository.save(token);

        //sending the email
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationCode);

    }

    @Transactional
    public boolean verifyEmail(String email, String verificationCode){
        Optional<EmailVerificationTokenEntity> tokenOpt = emailVerificationRepository.findByEmailAndVerificationCodeAndUsedFalse(email, verificationCode);

        if (tokenOpt.isPresent()){
            EmailVerificationTokenEntity token = tokenOpt.get();

            if (token.isExpired()){
                return false;
            }

            token.setUsed(true);
            emailVerificationRepository.save(token);


            UserBaseEntity user = userBaseRepository.findByEmail(email);
            if (user != null){
                user.setEmailVerified(true);
                userBaseRepository.save(user);
                return true;
            }
        }

        return false;
    }

    @Transactional
    public void handleEmailUpdate(UserBaseEntity user, String oldEmail, String newEmail){
        if (!oldEmail.equals(newEmail)){
            user.setEmailVerified(false);
            userBaseRepository.save(user);
            sendVerificationEmail(user);
        }
    }
}
