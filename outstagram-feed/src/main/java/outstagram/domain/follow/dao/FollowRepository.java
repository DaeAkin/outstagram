package outstagram.domain.follow.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import outstagram.domain.follow.domain.Follow;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends CrudRepository<Follow,Long> {
    Optional<Follow> findByFollowingIdAndFollowedId(Long followingId, Long followedId);
    void deleteByFollowingIdAndFollowedId(Long followingId, Long followedId);

    Optional<List<Follow>> findAllByFollowedId(Long followedId);
    Optional<List<Follow>> findAllByFollowingId(Long followingId);
}

