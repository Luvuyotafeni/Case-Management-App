package com.example.CaseManagement.controller;

import com.example.CaseManagement.Dto.AddDocumentRequest;
import com.example.CaseManagement.Dto.AssignLawyerRequest;
import com.example.CaseManagement.Dto.CreateCaseRequest;
import com.example.CaseManagement.entity.CaseEntity;
import com.example.CaseManagement.entity.DocumentEntity;
import com.example.CaseManagement.service.CaseService;
import com.example.CaseManagement.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createCase(@RequestBody CreateCaseRequest request,
                                        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String role = jwtTokenUtil.extractRole(token);

        // Optionally, you can add additional validation here based on role

        CaseEntity createdCase = caseService.createCase(
                request.getCaseName(),
                request.getCaseNumber(),
                request.getCaseType(),
                request.getDescription(),
                request.getStationName(),
                request.getProvince(),
                request.getOfficerName(),
                request.getOfficerContact(),
                request.getOccurrenceDate(),
                request.getUserId()
        );

        return ResponseEntity.ok(createdCase);
    }

    @PostMapping("/assign-lawyer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignLawyer(@RequestBody AssignLawyerRequest request) {
        CaseEntity updatedCase = caseService.assignLawyer(request.getCaseId(), request.getLawyerId());
        return ResponseEntity.ok(updatedCase);
    }

    @PostMapping("/add-document")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAWYER')")
    public ResponseEntity<?> addDocument(@RequestBody AddDocumentRequest request) {
        DocumentEntity document = caseService.addDocument(
                request.getCaseId(),
                request.getFileName(),
                request.getFileUrl(),
                request.getFileId()
        );
        return ResponseEntity.ok(document);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getCasesByUserId(@PathVariable Long userId) {
        List<CaseEntity> cases = caseService.getCasesByUserId(userId);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LAWYER')")
    public ResponseEntity<?> getCaseById(@PathVariable Long caseId) {
        CaseEntity caseEntity = caseService.getCaseById(caseId);
        return ResponseEntity.ok(caseEntity);
    }

    @GetMapping("/{caseId}/documents")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LAWYER')")
    public ResponseEntity<?> getDocumentsByCaseId(@PathVariable Long caseId) {
        List<DocumentEntity> documents = caseService.getDocumentsByCaseId(caseId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCases(){
        List<CaseEntity> cases = caseService.getAllCases();
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/lawyer/{lawyerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAWYER')")
    public ResponseEntity<?> getCaseByLawyerId(@PathVariable Long lawyerId){
        try{
            List<CaseEntity> cases = caseService.getCasesByLawyerUserId(lawyerId);
            return ResponseEntity.ok(cases);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("An unexpected Error occurred");
        }
    }
    @GetMapping("/lawyer-user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAWYER')")
    public ResponseEntity<?> getCasesByLawyerUserId(@PathVariable Long userId) {
        try {
            List<CaseEntity> cases = caseService.getCasesByLawyerUserId(userId);
            return ResponseEntity.ok(cases);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }
}