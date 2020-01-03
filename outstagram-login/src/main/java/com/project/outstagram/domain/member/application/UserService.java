package com.project.outstagram.domain.member.application;


import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.domain.User;
import com.project.outstagram.domain.member.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

/**
 * Spring-jdbc는 기본적으로 blocking 이기 때문에,
 * Stream 들이 모두 blocking 될 수 있다.
 * non-blocking 만들어 주기 위해 jdbc만 따로 스레드를 만드는 것이다.
 *
 * Conumser가 느린경우 -> ex) get,find.. etc  -> subscribeOn으로 스레드 생성. 아무데서나 체인 엮어도 됨.
 * Publisher가 느린 경우 -> ex) save ... etc -> publishOn 밑에 있는 것들만 스레드 생성
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder userPasswordEncoder;

    @Qualifier("jdbcScheduler")
    private final Scheduler jdbcScheduler;


    public Mono<Boolean> emailValidation(String email) {

//        System.out.println(userRepository.findByEmail(email).toString());

        return  Mono
                .defer(() -> Mono.just(userRepository.findByEmail(email)))
//                .doOnNext(u -> System.out.println("mock Email :" +  u.getEmail()))
                .subscribeOn(Schedulers.elastic())
                .thenReturn(true)
                .onErrorReturn(false);

//        return Mono.just(true);

    }

    public Mono<Boolean> joinUser(UserJoinRequest userJoinRequest) {
        return Mono.just(userJoinRequest)
                .publishOn(jdbcScheduler)
                .doOnNext( u -> {
                    User user = u.toEntity();
                    user.initialize(userPasswordEncoder);
                    userRepository.save(user);
                })
                .thenReturn(true).log();
//                .defaultIfEmpty(false).log();
    }


}
