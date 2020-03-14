package outstagram.test_fixture;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.application.FeedService;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;

import java.util.Arrays;
import java.util.List;

public class FeedFixtureGenerator {

    public static Feed makeOneFeedAndThreeMedia(FeedService feedService, Long userId) {
        List<MultipartFile> givenFileList = Arrays.asList(
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes())
        );
        FeedSaveRequest feedSaveRequest = FeedSaveRequest.builder()
                .content("인스타그램 #인스타 #팔로우 #헤헤")
                .build();
        return feedService.saveFeed(feedSaveRequest, givenFileList, userId);
    }
}
