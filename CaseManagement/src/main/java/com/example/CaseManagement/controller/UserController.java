package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.CreateUserRequest;
import com.example.CaseManagement.entity.UserEntity;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserRequest request) {
        UserEntity user = userService.createUser(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getProfilePictureUrl(),
                request.getPassword()
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @PathVariable Long userId,
            @RequestBody UserEntity updatedUser){
        UserEntity user = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(user);
    }
}