package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.CaseEntity;
import com.example.CaseManagement.entity.CourtDateEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.enumaration.Role;
import com.example.CaseManagement.repository.CaseRepository;
import com.example.CaseManagement.repository.CourtDateRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import com.example.CaseManagement.service.CourtDateService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CourtDateServiceImpl implements CourtDateService {

    @Autowired
    private CourtDateRepository courtDateRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private  EmailService emailService;

    private DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public CourtDateEntity createCourtDate(Long caseId, LocalDateTime scheduledDateTime, String courtName, String CourtRoom, String judgeAssigned, String hearingType, Long adminId) {

        CaseEntity caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case Not found with Id: "+ caseId));

        UserBaseEntity admin = userBaseRepository.findById(adminId)
                .orElseThrow(()-> new RuntimeException("Admin not found with Id: "+ adminId));

        if (!admin.getRole().equals(Role.ADMIN)){
            throw new RuntimeException("User with Id: "+ adminId + "is not an admin");
        }

        CourtDateEntity courtDate = new CourtDateEntity();
        courtDate.setRelatedCase(caseEntity);
        courtDate.setScheduledDateTime(scheduledDateTime);
        courtDate.setCourtName(courtName);
        courtDate.setJudgeAssigned(judgeAssigned);
        courtDate.setHearingType(hearingType);
        courtDate.setStatus("Scheduled");
        courtDate.setCreatedBy(adminId);

        String now = LocalDateTime.now().format(formatter);
        courtDate.setCreationDate(now);
        courtDate.setLastUpdated(now);


        CourtDateEntity savedCourtDate = courtDateRepository.save(courtDate);

        if (caseEntity.getUser() !=null){
            emailService.sendCourtDateNotification(
                    caseEntity.getUser().getEmail(),
                    caseEntity.getUser().getName(),
                    caseEntity.getCaseName(),
                    caseEntity.getCaseNumber(),
                    scheduledDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    courtName,
                    CourtRoom,
                    hearingType
            );
        }
        return savedCourtDate;
    }

    @Override
    @Transactional
    public CourtDateEntity updateCourtDate(Long courtDateId, LocalDateTime scheduledDateTime, String courtName, String courtRoom, String judgeAssigned, String hearingType, String status, Long adminId) {

        CourtDateEntity courtDate = courtDateRepository.findById(courtDateId)
                .orElseThrow(()-> new RuntimeException("Court Date not found with Id" + courtDateId));

        UserBaseEntity admin = userBaseRepository.findById(adminId)
                .orElseThrow(()-> new RuntimeException("Admin not found with Id: "+ adminId));

        if (!admin.getRole().equals(Role.ADMIN)){
            throw new RuntimeException("User with Id: "+ adminId + "is not and admin");
        }

        courtDate.setScheduledDateTime(scheduledDateTime);
        courtDate.setCourtName(courtName);
        courtDate.setCourtRoom(courtRoom);
        courtDate.setJudgeAssigned(judgeAssigned);
        courtDate.setHearingType(hearingType);
        courtDate.setStatus(status);
        courtDate.setLastUpdated(LocalDateTime.now().format(formatter));

        CourtDateEntity updatedCourtDate = courtDateRepository.save(courtDate);

        CaseEntity caseEntity = courtDate.getRelatedCase();
        if (caseEntity.getUser() != null){
            emailService.sendCourtDateUpdateNotification(
                    caseEntity.getUser().getEmail(),
                    caseEntity.getUser().getName(),
                    caseEntity.getCaseName(),
                    caseEntity.getCaseNumber(),
                    scheduledDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    courtName,
                    courtRoom,
                    hearingType,
                    status
            );
        }

        if (caseEntity.getAssignedlawyer() != null && caseEntity.getAssignedlawyer().getUser() != null){
            emailService.sendCourtDateUpdateNotification(
                    caseEntity.getAssignedlawyer().getUser().getEmail(),
                    caseEntity.getAssignedlawyer().getUser().getName(),
                    caseEntity.getCaseName(),
                    caseEntity.getCaseNumber(),
                    scheduledDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    courtName,
                    courtRoom,
                    hearingType,
                    status
            );
        }
        return updatedCourtDate;
    }

    @Override
    public void deleteCourtDate(Long courtDateId, Long adminId) {

        CourtDateEntity courtDate = courtDateRepository.findById(courtDateId)
                .orElseThrow(()-> new RuntimeException("court date not found with Id: " + courtDateId));

        UserBaseEntity admin = userBaseRepository.findById(adminId)
                .orElseThrow(()-> new RuntimeException("Admin nopt found with Id: "+ adminId));

        if (!admin.getRole().equals(Role.ADMIN)){
            throw new RuntimeException("User with Id: "+ admin+ "is not an admin");
        }

        courtDateRepository.delete(courtDate);

        CaseEntity caseEntity = courtDate.getRelatedCase();
        if (caseEntity.getUser() != null){
            emailService.sendCourtDateCancellationNotification(
                    caseEntity.getUser().getEmail(),
                    caseEntity.getUser().getName(),
                    caseEntity.getCaseName(),
                    caseEntity.getCaseNumber(),
                    courtDate.getScheduledDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        }

    }

    @Override
    public CourtDateEntity getCourtDateById(Long courtDateId) {
        return courtDateRepository.findById(courtDateId)
                .orElseThrow(()-> new RuntimeException("Court date not found with Id"+  courtDateId));
    }

    @Override
    public List<CourtDateEntity> getCourtDatesByCaseId(Long caseId) {
        return courtDateRepository.findByRelatedCase_CaseId(caseId);
    }

    @Override
    public List<CourtDateEntity> getCourtDatesByLawyerId(Long lawyerId) {
        return courtDateRepository.findByRelatedCase_Assignedlawyer_LawyerId(lawyerId);
    }

    @Override
    public List<CourtDateEntity> getCourtDatesByUserId(Long userId) {
        return courtDateRepository.findByRelatedCase_User_UserId(userId);
    }

    @Override
    public List<CourtDateEntity> getUpcomingCourtDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return courtDateRepository.findByScheduledDateTimeBetween(fromDate, toDate);
    }
}
