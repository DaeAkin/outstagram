package com.project.outstagram.domain.member.application;


import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.utils.UserFixture;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder userPasswordEncoder;

    @PostConstruct
    public void init() {
        UserFixture.makeUser(userPasswordEncoder,userRepository);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username"  + username);
        return userRepository.findByEmail(username).get();
    }
}
