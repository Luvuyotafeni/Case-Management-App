package com.example.CaseManagement.controller;


import com.example.CaseManagement.Dto.*;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.service.UserService;
import com.example.CaseManagement.service.impl.PasswordResetService;
import com.example.CaseManagement.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

            );

            UserBaseEntity user = userService.getUserByEmail(request.getEmail());

            String token = jwtTokenUtil.generateToken(
                    user.getEmail(),
                    user.getUserId(),
                    user.getRole().toString());

            LoginResponse response = new LoginResponse(user, token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e){
            throw new RuntimeException("Invalid email or password");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        passwordResetService.requestPasswordReset(request.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "if you email is registered, you will receive and otp shortly");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody ValidateOtpRequest request){
        boolean isValid = passwordResetService.validateOtp(request.getEmail(), request.getOtp());

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request){
        try{
            passwordResetService.resetPassword(
                    request.getEmail(),
                    request.getOtp(),
                    request.getNewPassword()
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", "password reset successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
