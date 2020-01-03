package com.project.outstagram.domain.member.api;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.project.outstagram.domain.member.application.UserService;
import com.project.outstagram.domain.member.domain.User;
import com.project.outstagram.domain.member.dto.EmailValidationRequest;
import com.project.outstagram.domain.member.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserApi {

    private final UserService userService;

    @PostMapping("/email-validation")
    public Mono validationEmail(@RequestBody @Valid EmailValidationRequest emailValidationRequest) {
        return userService.emailValidation(emailValidationRequest.getEmail());
    }


    @PostMapping("/join")
    public Mono<Void> joinUser(@RequestBody @Valid UserJoinRequest userJoinRequest) {
        return userService.joinUser(userJoinRequest);
    }

    @GetMapping("/whoami")
    public Mono<User> whoAmI(Authentication authentication) {
        Long id = Long.parseLong(authentication.getPrincipal().toString());
        return userService.getUserInfo(id);
    }










}
