package com.example.CaseManagement.controller;

import com.example.CaseManagement.entity.DocumentEntity;
import com.example.CaseManagement.service.CaseService;
import com.example.CaseManagement.service.impl.ImageKitService;
import com.example.CaseManagement.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private ImageKitService imageKitService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("caseId") Long caseId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Extract token and validate
            String token = authHeader.substring(7);
            String role = jwtTokenUtil.extractRole(token);

            // Upload file to ImageKit
            ImageKitService.ImageUploadResponse uploadResponse = imageKitService.uploadFile(file, caseId);

            // Store document info in database
            DocumentEntity document = caseService.addDocument(
                    caseId,
                    file.getOriginalFilename(),
                    uploadResponse.getFileUrl(),
                    uploadResponse.getFileId() // Store the fileId
            );

            return ResponseEntity.ok(document);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        try {
            String response = imageKitService.deleteFile(fileId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting file: " + e.getMessage());
        }
    }
}
