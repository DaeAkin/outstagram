package com.project.outstagram.domain.member.application;

import com.netflix.discovery.converters.Auto;
import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void 로그인시_아이디가있음() {
        //given
        Mono email = Mono.just("donghyeon@gmail.com");
        given(userRepository.findByEmail(email))
                .willReturn(Mono.just(new User(email.block().toString())));
        //when
        Mono result = userService.emailValidation(email);
        //then
        assertThat(result.block()).isEqualTo(true);
    }

    @Test
    public void 로그인시_아디디가없음() {
        Mono email = Mono.just("donghyeon@gmail.com");
        given(userRepository.findByEmail(email))
                .willReturn(Mono.empty());
        //when
        Mono result = userService.emailValidation(email);
        //then
        assertThat(result.block()).isEqualTo(false);

    }
}