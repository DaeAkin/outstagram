package com.project.outstagram;

import com.project.outstagram.user.UserFixture;
import com.project.outstagram.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class OutstaramApplication {
	@Autowired
	PasswordEncoder userPasswordEncoder;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(OutstaramApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void userInit() {
		UserFixture.makeUser(userPasswordEncoder,userRepository);
	}
}
