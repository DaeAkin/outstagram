package com.project.outstagram.domain.member.utils;

import com.project.outstagram.domain.member.dto.User;
import com.project.outstagram.domain.member.dao.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserFixture {

    public static User makeUser(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        User user = new User("donghyeon","1234");
        user.initialize(passwordEncoder);

        Optional<User> donghyeon = userRepository.findByEmail("donghyeon");

        if(!donghyeon.isPresent()){
            userRepository.save(user);
        }
        return user;
    }
}
