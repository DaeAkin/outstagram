package outstagram.domain.follow.dao;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import outstagram.domain.follow.domain.Follow;
import reactor.core.publisher.Mono;

@Repository
public interface FollowRepository extends ReactiveCrudRepository<Follow,Long> {
    Mono<Follow> findByFollowingIdAndFollowedId(Long followingId, Long followedId);
    Mono<Void> deleteByFollowingIdAndFollowedId(Long followingId, Long followedId);
}

