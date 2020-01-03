package com.project.outstagram.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class EmailValidationRequest {
    @NotBlank
    String email;
}
