package outstagram.domain.follow.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import outstagram.domain.follow.domain.Follow;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface FollowRepository extends ReactiveMongoRepository<Follow,Long> {
    Mono<Follow> findByFollowingIdAndFollowedId(Long followingId, Long followedId);
    Mono<Void> deleteByFollowingIdAndFollowedId(Long followingId, Long followedId);
}

