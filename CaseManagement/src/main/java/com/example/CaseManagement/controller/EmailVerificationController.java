package com.example.CaseManagement.controller;


import com.example.CaseManagement.Dto.EmailVerificationRequest;
import com.example.CaseManagement.Dto.ResendVerificationRequest;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.service.UserService;
import com.example.CaseManagement.service.impl.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/verification")
public class EmailVerificationController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private UserService userService;

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerificationRequest request){
        boolean verified = emailVerificationService.verifyEmail(request.getEmail(), request.getVerificationCode());

        Map<String, Object> response = new HashMap<>();
        if (verified){
            response.put("Success", true);
            response.put("message", "Email verified successfully");
        } else {
            response.put("success", true);
            response.put("message", "Invalid or expired verification code");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody ResendVerificationRequest request){
        try {
            UserBaseEntity user = userService.getUserByEmail(request.getEmail());
            emailVerificationService.sendVerificationEmail(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verification code sent successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to send verification code: "+ e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
