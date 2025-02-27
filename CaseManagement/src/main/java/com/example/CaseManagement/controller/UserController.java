package com.example.CaseManagement.controller;


import com.example.CaseManagement.entity.UserEntity;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String profilePictureUrl,
            @RequestParam String password) {
        UserEntity user = userService.createUser(name, email, phone, profilePictureUrl, password);
        return ResponseEntity.ok(user);
    }
}
