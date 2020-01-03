package com.project.outstagram.domain.member.dto;

import lombok.Data;

@Data
public class EmailValidationResponse {
    boolean validationResult;


    public EmailValidationResponse(boolean validationResult) {
        this.validationResult = validationResult;
    }
}
