package com.project.outstagram.domain.member.dao;


import com.project.outstagram.domain.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveCrudRepository {
    Mono<User> findByEmail(Mono<String> email);
}
