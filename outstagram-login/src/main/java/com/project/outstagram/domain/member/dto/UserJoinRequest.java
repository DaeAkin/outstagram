package com.project.outstagram.domain.member.dto;

import com.project.outstagram.domain.member.domain.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserJoinRequest {
    @NotBlank(message = "이메일을 다시 확인해주세요.")
    String email;

    @NotBlank(message = "비밀번호를 다시 확인해주세요.")
    String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

}
