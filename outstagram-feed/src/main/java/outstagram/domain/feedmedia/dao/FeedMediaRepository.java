package outstagram.domain.feedmedia.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import outstagram.domain.feedmedia.domain.FeedMedia;

@Repository
public interface FeedMediaRepository extends CrudRepository<FeedMedia,Long> {
}
