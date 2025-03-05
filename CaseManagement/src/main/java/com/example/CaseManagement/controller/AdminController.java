package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.CreateAdminRequest;
import com.example.CaseManagement.entity.AdminEntity;
import com.example.CaseManagement.service.AdminService;
import com.example.CaseManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

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

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminEntity> getAdminById(@PathVariable Long adminId){
        return  ResponseEntity.ok(adminService.getAdminById(adminId));
    }

    @GetMapping
    public  ResponseEntity<List<AdminEntity>> getAllAdmins(){
        return ResponseEntity.ok(adminService.getAllAdmin());
    }

    @PutMapping("/update/{adminId}")
    public ResponseEntity<AdminEntity> updateAdmin(@PathVariable Long adminId, @RequestBody AdminEntity updatedAdmin){
        return ResponseEntity.ok(adminService.updateAdmin(adminId, updatedAdmin));
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId){
        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

}
