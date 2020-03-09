package outstagram.domain.feedmedia.application;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedMediaService {

    public boolean saveFeedMedia(Long userId,List<MultipartFile> feedMediaList);
}
