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
                "This code is valid for 10 minutes. If you did not create an account with us, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The Case Management App Team";
        sendEmail(to, subject,body);
    }

    public void sendCaseCreationConfirmation(String to, String userName,String caseName, String caseNumber){
        String subject = "Case Creation Confirmation - Case Number: " + caseNumber;
        String body = "Dear "+ userName + "\n\n"+
                "Thank you for submitting your case \""+ caseName+"\"(Casenumber: "+ caseNumber + "). \n\n "+
                "We want to assure you that your case has been received and will be handled with the utmost responsibility and care.\n\n" +
                "To proceed with the case processing, please upload all relevant documents and link them to this case through the 'Open Cases' section of your account.\n\n" +
                "If you have any questions or need assistance, please don't hesitate to contact our support team.\n\n" +
                "Best regards,\n" +
                "The Case Management Team";
        sendEmail(to, subject, body);
    }
}
