package com.project.outstagram.domain.member.application;

import com.netflix.discovery.converters.Auto;
import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.domain.User;
import com.project.outstagram.domain.member.dto.UserJoinRequest;
import com.project.outstagram.test.MockTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.Executors;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class UserServiceTest extends MockTest {

    @InjectMocks
    UserService userService;



    Scheduler scheduler = Schedulers.fromExecutor(Executors.newFixedThreadPool(10));
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder userPasswordEncoder;

    private static final String email = "donghyeon@gmail.com";

    @Test
    public void 로그인시_아이디가있음() {
        //given
        given(userRepository.findByEmail(email))
                .willReturn(new User(email));
        //then
        StepVerifier.create(userService.emailValidation(email))
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    @Test
    public void 로그인시_아디디가없음() {
        given(userRepository.findByEmail(email))
                .willReturn(null);

        //then
        StepVerifier.create(userService.emailValidation(email))
                .expectNext(false)
                .expectComplete()
                .verify();

    }

//    @Test
//    public void 회원가입_테스트() {
//        //given
//        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
//                .email(email)
//                .password("51231")
//                .build();
//        given(userRepository.save(userJoinRequest.toEntity())).
//                willReturn(userJoinRequest.toEntity());
//
//        //when
//        Mono<Boolean> result = userService.joinUser(userJoinRequest);
//
//        //then
//        assertThat(result.block()).isEqualTo(true);
//
//    }
}