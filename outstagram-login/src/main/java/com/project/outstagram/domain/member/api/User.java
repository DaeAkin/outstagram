package com.project.outstagram.domain.member.api;

import com.project.outstagram.domain.member.application.UserService;
import com.project.outstagram.domain.member.dto.EmailValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class User {

    private final UserService userService;

    @PostMapping
    public Publisher validationEmail(@RequestBody EmailValidationRequest emailValidationRequest) {
        return userService.emailValidation(emailValidationRequest.getEmail());
    }



}
