package com.example.CaseManagement.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateOtpRequest {

    private String email;
    private String otp;
}
