package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.CreateUserRequest;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.entity.UserEntity;
import com.example.CaseManagement.enumaration.Role;
import com.example.CaseManagement.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


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

    @GetMapping("/{userId}")
    public ResponseEntity<UserBaseEntity> getUserBaseById(@PathVariable Long userId){
        UserBaseEntity user = userService.getUserBaseById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserBaseEntity>> getUsersByRole(@PathVariable Role role){
        List<UserBaseEntity> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<UserEntity> updateProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            UserEntity updatedUser = userService.updateProfilePicture(userId, file);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}