package com.example.CaseManagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendResetOtpEmail(String to, String otp){
        String subject = "Password Reset otp";
        String body = "Your OTP for password reset is : " + otp + "\n\n" +
                "This OTP is valid for 10 minutes. if you did not request a password reset, please ignore the email.";

        sendEmail(to, subject, body);
    }

    public void sendVerificationEmail(String to, String name, String verificationCode){
        String subject = "Welcome to Case Management App - Email Verification";
        String body = "Dear " + name + ",\n\n" +
                "Welcome to the Case Management App! We're excited to have you on board.\n\n" +
                "To verify your email address, please use the following verification code: " + verificationCode + "\n\n" +
                "This code is valid for 24 hours. If you did not create an account with us, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The Case Management App Team";
        sendEmail(to, subject,body);
    }
}
