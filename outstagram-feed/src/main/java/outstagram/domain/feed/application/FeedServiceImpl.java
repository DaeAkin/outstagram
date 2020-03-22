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
import outstagram.domain.feed.dto.FeedUpdateRequest;
import outstagram.domain.feedmedia.domain.FeedMedia;
import outstagram.global.exception.NoDataException;
import outstagram.global.utils.MediaUtil;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    @Value("${resource-path}media")
    private final String resourcePath = System.getProperty("user.home") + File.separator + "outstagram-resource-test" + File.separator;

    private final RestTemplate restTemplate;
    private final FeedRepository feedRepository;



    @Override
    public Feed saveFeed(FeedSaveRequest feedSaveRequest, List<MultipartFile> mediaFile, Long userId) {
        Objects.requireNonNull(mediaFile);

        return Optional.of(feedRepository.save(feedSaveRequest.toEntity(userId)))
                .map(f -> {
                     f.analysisContentByHashTags(restTemplate);
                    saveFeedMedia(mediaFile, f);
                    return feedRepository.save(f);
                }).get();
    }

    @Override
    public Feed updateFeed(FeedUpdateRequest feedUpdateRequest, Long userId,Long feedId) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if(!optionalFeed.isPresent())
            throw new NoDataException(1004L,"잠시 후 다시 시도해주세요.");

        Feed feed = optionalFeed.get();
        feed.updateFeed(feedUpdateRequest);
        return feed;
    }

    private void saveFeedMedia(List<MultipartFile> mediaFile, Feed feed) {
        mediaFile
                .forEach(fm ->{
                    log.info("save Feed Media");
                    String path = MediaUtil.saveImageFile(fm, resourcePath, "");
                    FeedMedia feedMedia = new FeedMedia(path);
                    feedMedia.setFeed(feed);
                });

    }
    @Override
    public void deleteFeed(Long userId, Long feedId) {
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if(!optionalFeed.isPresent()) {
            //do Something...
            throw new RuntimeException();
        }
        if (!optionalFeed.get().isOwner(userId)) {
            //do Something..
            throw new RuntimeException();
        }
        feedRepository.delete(optionalFeed.get());
    }

    @Override
    public Feed getFeedByFeedId(Long userId, Long feedId) {
        //친구 인지 확인 or 비공개 인지 확인

        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if(!optionalFeed.isPresent())


        return null;
    }

}
