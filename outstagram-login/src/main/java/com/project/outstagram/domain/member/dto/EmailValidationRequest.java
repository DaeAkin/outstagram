package com.project.outstagram.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class EmailValidationRequest {
    @NotBlank(message = "email 형식이 올바르지 않습니다.")
    String email;
}
