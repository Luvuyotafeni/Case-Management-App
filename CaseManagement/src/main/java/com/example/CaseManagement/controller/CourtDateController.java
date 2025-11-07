package com.example.CaseManagement.controller;

import com.example.CaseManagement.entity.CourtDateEntity;
import com.example.CaseManagement.service.CourtDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/court-dates")
public class CourtDateController {

    @Autowired
    private CourtDateService courtDateService;

    @PostMapping("/create")
    public ResponseEntity<CourtDateEntity> createCourtDate(@RequestBody Map<String, Object> request) {
        try {
            Long caseId = Long.parseLong(request.get("caseId").toString());
            LocalDateTime scheduledDateTime = LocalDateTime.parse(request.get("scheduledDateTime").toString());
            String courtName = request.get("courtName").toString();
            String courtRoom = request.get("courtRoom").toString();
            String judgeAssigned = request.get("judgeAssigned").toString();
            String hearingType = request.get("hearingType").toString();
            Long adminId = Long.parseLong(request.get("adminId").toString());

            CourtDateEntity courtDate = courtDateService.createCourtDate(
                    caseId, scheduledDateTime, courtName, courtRoom,
                    judgeAssigned, hearingType, adminId
            );

            return new ResponseEntity<>(courtDate, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{courtDateId}")
    public ResponseEntity<CourtDateEntity> updateCourtDate(
            @PathVariable Long courtDateId,
            @RequestBody Map<String, Object> request) {
        try {
            LocalDateTime scheduledDateTime = LocalDateTime.parse(request.get("scheduledDateTime").toString());
            String courtName = request.get("courtName").toString();
            String courtRoom = request.get("courtRoom").toString();
            String judgeAssigned = request.get("judgeAssigned").toString();
            String hearingType = request.get("hearingType").toString();
            String status = request.get("status").toString();
            Long adminId = Long.parseLong(request.get("adminId").toString());

            CourtDateEntity courtDate = courtDateService.updateCourtDate(
                    courtDateId, scheduledDateTime, courtName, courtRoom,
                    judgeAssigned, hearingType, status, adminId
            );

            return new ResponseEntity<>(courtDate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{courtDateId}")
    public ResponseEntity<Void> deleteCourtDate(
            @PathVariable Long courtDateId,
            @RequestParam Long adminId) {
        try {
            courtDateService.deleteCourtDate(courtDateId, adminId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{courtDateId}")
    public ResponseEntity<CourtDateEntity> getCourtDateById(@PathVariable Long courtDateId) {
        try {
            CourtDateEntity courtDate = courtDateService.getCourtDateById(courtDateId);
            return new ResponseEntity<>(courtDate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<CourtDateEntity>> getCourtDatesByCaseId(@PathVariable Long caseId) {
        try {
            List<CourtDateEntity> courtDates = courtDateService.getCourtDatesByCaseId(caseId);
            return new ResponseEntity<>(courtDates, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<CourtDateEntity>> getCourtDatesByLawyerId(@PathVariable Long lawyerId) {
        try {
            List<CourtDateEntity> courtDates = courtDateService.getCourtDatesByLawyerId(lawyerId);
            return new ResponseEntity<>(courtDates, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourtDateEntity>> getCourtDatesByUserId(@PathVariable Long userId) {
        try {
            List<CourtDateEntity> courtDates = courtDateService.getCourtDatesByUserId(userId);
            return new ResponseEntity<>(courtDates, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<CourtDateEntity>> getUpcomingCourtDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        try {
            List<CourtDateEntity> courtDates = courtDateService.getUpcomingCourtDates(fromDate, toDate);
            return new ResponseEntity<>(courtDates, HttpStatus.OK);
        } catch (Exception e) { 
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}