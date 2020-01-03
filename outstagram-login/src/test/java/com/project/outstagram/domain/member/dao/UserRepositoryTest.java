package com.project.outstagram.domain.member.dao;

import com.netflix.discovery.converters.Auto;
import com.project.outstagram.test.RepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.junit.Assert.*;

public class UserRepositoryTest extends RepositoryTest {

//    @Autowired
//    UserRepository userRepository;

    @Test
    public void test() {
      System.out.println("hi");
    }
}