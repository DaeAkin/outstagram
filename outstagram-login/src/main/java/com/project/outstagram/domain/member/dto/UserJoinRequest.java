package com.project.outstagram.domain.member.dto;

import com.project.outstagram.domain.member.domain.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserJoinRequest {
    @NotBlank
    String email;
    @NotBlank
    String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

}
