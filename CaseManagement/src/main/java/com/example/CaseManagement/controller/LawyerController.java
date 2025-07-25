package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.CreateLawyerRequest;
import com.example.CaseManagement.entity.LawyerEntity;
import com.example.CaseManagement.service.LawyerService;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/lawyers")
public class LawyerController {


    @Autowired
    private UserService userService;

    @Autowired
    private LawyerService lawyerService;

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

    @GetMapping("/{lawyerId}")
    public ResponseEntity<LawyerEntity> getLawyerById(@PathVariable Long lawyerId){
        LawyerEntity lawyer = lawyerService.getLawyerById(lawyerId);
        return ResponseEntity.ok(lawyer);
    }

    @GetMapping
    public ResponseEntity<List<LawyerEntity>> getAllLawyers(){
        List<LawyerEntity> lawyer = lawyerService.getAllLawyers();
        return ResponseEntity.ok(lawyer);
    }

    @PutMapping("/update/{lawyerId}")
    public ResponseEntity<LawyerEntity> updateLawyer(@PathVariable Long lawyerId, @RequestBody LawyerEntity updatedLayer){
        LawyerEntity lawyer = lawyerService.updateLawyerByUserId(lawyerId, updatedLayer);
        return ResponseEntity.ok(lawyer);
    }

    @DeleteMapping("/delete/{lawyerId}")
    public ResponseEntity<Void> deleteLawyer(@PathVariable Long lawyerId){
        lawyerService.deleteLawyer(lawyerId);
        return ResponseEntity.noContent().build();
    }
}
