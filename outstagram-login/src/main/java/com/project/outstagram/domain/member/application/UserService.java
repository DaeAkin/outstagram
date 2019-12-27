package com.project.outstagram.domain.member.application;


import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService{

    private final UserRepository userRepository;


    public Mono<Boolean> emailValidation(Mono<String> email) {
        Mono<User> user = userRepository.findByEmail(email);
        return user.map(u -> true).switchIfEmpty(Mono.just(false));
    }



}
