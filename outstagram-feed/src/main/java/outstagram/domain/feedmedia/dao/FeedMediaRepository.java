package outstagram.domain.feedmedia.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import outstagram.domain.feedmedia.domain.FeedMedia;

public interface FeedMediaRepository extends JpaRepository<FeedMedia,Long> {
}
