package com.example.CaseManagement.service;

import com.example.CaseManagement.entity.CourtDateEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CourtDateService {

    CourtDateEntity createCourtDate(Long caseId, LocalDateTime scheduledDateTime,
                                    String courtName, String CourtRoom, String judgeAssigned,
                                    String hearingType, Long adminId);

    CourtDateEntity updateCourtDate(Long courtDateId, LocalDateTime scheduledDateTime,
                                    String courtName, String courtRoom, String judgeAssigned,
                                    String hearingType,String status, Long adminId);

    void deleteCourtDate(Long courtDateId, Long adminId);

    CourtDateEntity getCourtDateById(Long courtDateId);

    List<CourtDateEntity> getCourtDatesByCaseId(Long caseId);

    List<CourtDateEntity> getCourtDatesByLawyerId(Long lawyerId);

    List<CourtDateEntity> getCourtDatesByUserId(Long userId);

    List<CourtDateEntity> getUpcomingCourtDates(LocalDateTime fromDate, LocalDateTime toDate);
}
