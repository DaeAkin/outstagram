package com.project.outstagram.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserFixture {

    public static User makeUser(PasswordEncoder passwordEncoder,UserRepository userRepository) {
        User user = new User("donghyeon","1234");
        user.initialize(passwordEncoder);

        Optional<User> donghyeon = userRepository.findByUsername("donghyeon");

        if(!donghyeon.isPresent()){
            userRepository.save(user);
        }
        return user;
    }
}
