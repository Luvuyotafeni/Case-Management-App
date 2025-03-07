package com.example.CaseManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AssignLawyerRequest {
    private Long caseId;
    private Long lawyerId;
}
