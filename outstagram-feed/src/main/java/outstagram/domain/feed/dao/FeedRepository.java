package outstagram.domain.feed.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import outstagram.domain.feed.domain.Feed;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed,Long> {
    Optional<Feed> findFeedByUserId(Long userId);
}
