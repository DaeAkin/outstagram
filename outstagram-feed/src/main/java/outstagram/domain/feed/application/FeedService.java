package outstagram.domain.feed.application;

import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.domain.feed.dto.FeedUpdateRequest;

import java.util.List;

public interface FeedService {

    Feed saveFeed(FeedSaveRequest feedSaveRequest, List<MultipartFile> mediaFile, Long userId);
    Feed updateFeed(FeedUpdateRequest feedUpdateRequest ,Long userId,Long feedId);
    void deleteFeed(Long userId,Long feedId);
    Feed getFeedByFeedId(Long userId,Long feedId);
}
