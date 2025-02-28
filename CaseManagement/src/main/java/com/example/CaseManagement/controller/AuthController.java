package com.example.CaseManagement.controller;


import com.example.CaseManagement.Dto.LoginRequest;
import com.example.CaseManagement.Dto.LoginResponse;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        try {
            UserBaseEntity user = userService.authenticateUser(request.getEmail(), request.getPassword());
            LoginResponse response = new LoginResponse(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            throw e;
        }
    }
}
