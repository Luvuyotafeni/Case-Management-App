package com.example.CaseManagement.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CreateCaseRequest {
    private String caseName;
    private String caseNumber;
    private String caseType;
    private String description;
    private String stationName;
    private String province;
    private String officerName;
    private String officerContact;
    private String occurrenceDate;
    private Long userId;
}
