package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.CreateLawyerRequest;
import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/lawyers")
public class LawyerController {


    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LawyerEntity> createLawyer(@RequestBody CreateLawyerRequest request) {
        LawyerEntity lawyer = userService.createLawyer(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getProfilePictureUrl(),
                request.getSpecialization(),
                request.getPassword()
        );

        return ResponseEntity.ok(lawyer);
    }
}
