package outstagram.domain.follow.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import outstagram.domain.follow.domain.Follow;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long>{
    Follow findByFollowingIdAndFollowedId(Long followingId, Long followedId);
    void deleteByFollowingIdAndFollowedId(Long followingId, Long followedId);
}

