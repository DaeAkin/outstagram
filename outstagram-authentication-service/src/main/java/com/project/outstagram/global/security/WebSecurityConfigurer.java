package com.project.outstagram.global.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userService;
    @Autowired
    private PasswordEncoder userPasswordEncoder;

    @Override
    @Bean
    //스프링 시큐리티가 인증을 처리하는데 사용됨.
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    // 스프링 시큐리티에서 반환될 사용자 정보를 저장하는 데 사용됨.
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return userService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userService)
                .passwordEncoder(userPasswordEncoder);
//                .withUser("john.carnell").password(encoder.encode("password1")).roles("USER")
//                .and()
//                .withUser("william.woodward").password(encoder.encode("password2")).roles("USER", "ADMIN");
    }
}