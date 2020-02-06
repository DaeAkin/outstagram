package outstagram.domain.feed.application;

import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.dto.FeedSaveRequest;

import java.util.List;

public interface FeedService {

    public void saveFeed(FeedSaveRequest feedSaveRequest, List<MultipartFile> mediaFile, Long userId);
}
