package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.CreateAdminRequest;
import com.example.CaseManagement.entity.AdminEntity;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<AdminEntity> createAdmin(@RequestBody CreateAdminRequest request){
        AdminEntity admin = userService.createAdmin(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getProfilePictureUrl(),
                request.getAdminSpecificColumn(),
                request.getPassword()
        );

        return ResponseEntity.ok(admin);
    }

}
