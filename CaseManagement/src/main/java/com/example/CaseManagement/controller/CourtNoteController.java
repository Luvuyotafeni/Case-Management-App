package com.example.CaseManagement.controller;

import com.example.CaseManagement.entity.CourtNoteEntity;
import com.example.CaseManagement.service.CourtNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/court-notes")
public class CourtNoteController {

    @Autowired
    private CourtNoteService courtNoteService;

    @PostMapping("/create")
    public ResponseEntity<CourtNoteEntity> createCourtNote(@RequestBody Map<String, Object> request) {
        try {
            Long caseId = Long.parseLong(request.get("caseId").toString());

            Long courtDateId = null;
            if (request.containsKey("courtDateId") && request.get("courtDateId") != null) {
                courtDateId = Long.parseLong(request.get("courtDateId").toString());
            }

            String noteContent = request.get("noteContent").toString();
            Long createdById = Long.parseLong(request.get("createdById").toString());
            String createdByRole = request.get("createdByRole").toString();

            CourtNoteEntity courtNote = courtNoteService.createCourtNote(
                    caseId, courtDateId, noteContent, createdById, createdByRole
            );

            return new ResponseEntity<>(courtNote, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<CourtNoteEntity> updateCourtNote(
            @PathVariable Long noteId,
            @RequestBody Map<String, Object> request) {
        try {
            String noteContent = request.get("noteContent").toString();
            Long updatedById = Long.parseLong(request.get("updatedById").toString());

            CourtNoteEntity courtNote = courtNoteService.updateCourtNote(
                    noteId, noteContent, updatedById
            );

            return new ResponseEntity<>(courtNote, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteCourtNote(
            @PathVariable Long noteId,
            @RequestParam Long adminId) {
        try {
            courtNoteService.deleteCourtNote(noteId, adminId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<CourtNoteEntity> getCourtNoteById(@PathVariable Long noteId) {
        try {
            CourtNoteEntity courtNote = courtNoteService.getCourtNoteById(noteId);
            return new ResponseEntity<>(courtNote, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<CourtNoteEntity>> getCourtNotesByCaseId(@PathVariable Long caseId) {
        try {
            List<CourtNoteEntity> courtNotes = courtNoteService.getCourtNotesByCaseId(caseId);
            return new ResponseEntity<>(courtNotes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/court-date/{courtDateId}")
    public ResponseEntity<List<CourtNoteEntity>> getCourtNotesByCourtDateId(@PathVariable Long courtDateId) {
        try {
            List<CourtNoteEntity> courtNotes = courtNoteService.getCourtNotesByCourtDateId(courtDateId);
            return new ResponseEntity<>(courtNotes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<CourtNoteEntity>> getCourtNotesByLawyerId(@PathVariable Long lawyerId) {
        try {
            List<CourtNoteEntity> courtNotes = courtNoteService.getCourtNotesByLawyerId(lawyerId);
            return new ResponseEntity<>(courtNotes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourtNoteEntity>> getCourtNotesByUserId(@PathVariable Long userId) {
        try {
            List<CourtNoteEntity> courtNotes = courtNoteService.getCourtNotesByUserId(userId);
            return new ResponseEntity<>(courtNotes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}