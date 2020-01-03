package com.project.outstagram.domain.member.application;

import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.domain.User;
import com.project.outstagram.domain.member.dto.EmailValidationResponse;
import com.project.outstagram.domain.member.dto.UserJoinRequest;
import com.project.outstagram.test.MockTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;


public class UserServiceTest extends MockTest {

    @InjectMocks
    UserService userService;

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
                .expectNext(new EmailValidationResponse(true))
                .expectComplete()
                .verify();
    }

    @Test
    public void 로그인시_아디디가없음() {
        given(userRepository.findByEmail(email))
                .willReturn(null);

        //then
        StepVerifier.create(userService.emailValidation(email))
                .expectNext(new EmailValidationResponse(false))
                .expectComplete()
                .verify();

    }

    @Test
    public void 회원가입_테스트() {
        //given
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .email(email)
                .password("51231")
                .build();

        //then
        StepVerifier.create(userService.joinUser(userJoinRequest))
                .expectComplete()
                .verify();
    }
}