package outstagram.domain.feed.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import outstagram.domain.feed.domain.Feed;

public interface FeedRepository extends JpaRepository<Feed,Long> {
}
