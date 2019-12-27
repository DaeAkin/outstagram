package com.project.outstagram.domain.emailauth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailAuthService {

    public void sendAuthEmail(Mono<String> email) {

    }

    public Mono authEmail(Mono<String> token) {

        return null;
    }
}
