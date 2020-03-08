package outstagram.domain.feed.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.dao.FeedRepository;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.domain.feedmedia.application.FeedMediaService;
import outstagram.domain.feedmedia.dao.FeedMediaRepository;
import outstagram.global.utils.MediaUtil;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{

    private final RestTemplate restTemplate;
    private final FeedRepository feedRepository;
    private final FeedMediaService feedMediaService;


    @Override
    public void saveFeed(FeedSaveRequest feedSaveRequest, List<MultipartFile> mediaFile, Long userId) {
        Optional.of(feedRepository.save(feedSaveRequest.toEntity()))
                .map(f -> {
                     f.analysisContentByHashTags(restTemplate,feedSaveRequest.getContent());
                     return feedMediaService.saveFeedMedia(mediaFile);
                });
    }
}
