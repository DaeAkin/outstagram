package outstagram.domain.feed.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.dao.FeedRepository;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.domain.feedmedia.domain.FeedMedia;
import outstagram.global.utils.MediaUtil;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    @Value("${resource-path}media")
    private final String resourcePath = System.getProperty("user.home") + File.separator + "outstagram-resource-test" + File.separator;

    private final RestTemplate restTemplate;
    private final FeedRepository feedRepository;
    private final FeedMediaService feedMediaService;


    @Override
    public void saveFeed(FeedSaveRequest feedSaveRequest, List<MultipartFile> mediaFile, Long userId) {
        Optional.of(feedRepository.save(feedSaveRequest.toEntity(userId)))
                .map(f -> {
                     f.analysisContentByHashTags(restTemplate);
                     saveFeedMedia(mediaFile,f);
                     return null;
                });
    }

    private void saveFeedMedia(List<MultipartFile> mediaFile, Feed feed) {
        mediaFile
                .forEach(fm ->{
                    String path = MediaUtil.saveImageFile(fm, resourcePath, "");
                    FeedMedia feedMedia = new FeedMedia(path);
                    feedMedia.setFeed(feed);
                });

    }
}
