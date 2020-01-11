package outstagram.domain.follow.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import outstagram.domain.follow.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow,Long> {
}
