package com.project.outstagram.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private UserDetailsService userService;

    public PasswordEncoder userPasswordEncoder;



    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(userPasswordEncoder);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //@formatter:off
//        auth.inMemoryAuthentication()
//                .withUser("habuma").password("password").authorities("ROLE_USER", "ROLE_ADMIN")
//                .and()
//                .withUser("izzy").password("password").authorities("ROLE_USER");
//
//        //@formatter:on
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return userPasswordEncoder;
    }

}
