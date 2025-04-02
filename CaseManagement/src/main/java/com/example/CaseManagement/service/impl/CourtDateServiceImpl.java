package com.example.CaseManagement.service.impl;

import com.example.CaseManagement.entity.CaseEntity;
import com.example.CaseManagement.entity.CourtDateEntity;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.enumaration.Role;
import com.example.CaseManagement.repository.CaseRepository;
import com.example.CaseManagement.repository.CourtDateRepository;
import com.example.CaseManagement.repository.UserBaseRepository;
import com.example.CaseManagement.service.CourtDateService;
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
        }
    }

    @Override
    public CourtDateEntity updateCourtDate(Long courtDateId, LocalDateTime scheduledDateTime, String courtName, String courtRoom, String judgeAssigned, String hearingType, String status, Long adminId) {
        return null;
    }

    @Override
    public void deleteCourtDate(Long courtDateId, Long adminId) {

    }

    @Override
    public CourtDateEntity getCourtDateById(Long courtDateId) {
        return null;
    }

    @Override
    public List<CourtDateEntity> getCourtDatesByCaseId(Long caseId) {
        return List.of();
    }

    @Override
    public List<CourtDateEntity> getCourtDatesByLawyerId(Long lawyerId) {
        return List.of();
    }

    @Override
    public List<CourtDateEntity> getCourtDatesByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<CourtDateEntity> getUpcomingCourtDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return List.of();
    }
}
