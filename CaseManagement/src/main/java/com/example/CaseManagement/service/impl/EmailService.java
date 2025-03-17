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
}
