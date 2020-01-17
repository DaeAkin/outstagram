package outstagram.domain.follow.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import outstagram.domain.follow.domain.Follow;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface FollowRepository extends CrudRepository<Follow,Long> {
    Optional<Follow> findByFollowingIdAndFollowedId(Long followingId, Long followedId);
    void deleteByFollowingIdAndFollowedId(Long followingId, Long followedId);
}

