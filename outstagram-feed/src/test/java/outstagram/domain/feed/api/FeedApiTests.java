package outstagram.domain.feed.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.api.FeedApi;
import outstagram.domain.feed.application.FeedService;
import outstagram.domain.feed.dao.FeedRepository;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.domain.feed.dto.FeedUpdateRequest;
import outstagram.domain.feedmedia.dao.FeedMediaRepository;
import outstagram.domain.feedmedia.domain.FeedMedia;
import outstagram.test.IntegrationTest;
import outstagram.test_fixture.FeedFixtureGenerator;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
public class FeedApiTests extends IntegrationTest {
//
//    @Autowired
//    TestRestTemplate testRestTemplate;

    @Autowired
    FeedApi feedApi;
    @Autowired
    FeedRepository feedRepository;
    @Autowired
    FeedMediaRepository feedMediaRepository;
    @Autowired
    FeedService feedService;

    @MockBean
    Authentication authentication;

    Long userId = 5L;
    @Test
    public void 피드_작성하기_테스트() {
        //given
        String content = "#인스타 안녕하세요 인스타그램 입니다. 반가워요" +
                "#인스타그램 #맞팔 #헤헤헤 ";
        List<MultipartFile> givenFileList = Arrays.asList(
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes())
        );
        FeedSaveRequest feedSaveRequest = new FeedSaveRequest(content);
        when(authentication.getPrincipal()).thenReturn(userId);

        //when
        ResponseEntity<Feed> response = feedApi.saveMyFeed(feedSaveRequest, givenFileList, authentication);
        //then
        Feed feed = feedRepository.findFeedByUserId(userId).get();
        assertThat(((List<FeedMedia>) feedMediaRepository.findAll()).size()).isEqualTo(3);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getUserId()).isEqualTo(userId);
        assertThat(response.getBody().getFeedMediaList().get(0).getResourceLocation()).isNotBlank();
    }

    @Test
    public void 피드_삭제하기_테스트() {
        //given
        Feed feed = FeedFixtureGenerator.makeOneFeedAndThreeMedia(feedService, userId);
        when(authentication.getPrincipal()).thenReturn(userId);
        //when
        feedApi.deleteMyFeed(authentication,feed.getId());
        //then
        assertThat(feedRepository.findById(feed.getId())).isEmpty();
        assertThat(feedMediaRepository.findAll()).isEmpty();
    }

    @Test
    public void 피드_업데이트하기_테스트() {
        //given
        String updateContent = "수정 #인스타 #수정";
        Feed feed = FeedFixtureGenerator.makeOneFeedAndThreeMedia(feedService, userId);
        when(authentication.getPrincipal()).thenReturn(userId);
        FeedUpdateRequest feedUpdateRequest = FeedUpdateRequest.builder()
                .feedId(feed.getId())
                .content(updateContent)
                .build();
        //when
        ResponseEntity<Feed> response = feedApi.updateMyFeed(feedUpdateRequest, authentication);
        //then
        assertThat(Objects.requireNonNull(response.getBody()).getContent()).isEqualTo(updateContent);
        assertThat(response.getBody().getFeedMediaList()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(feed.getId());
    }

//    @Test(expected = )
//    public void 피드_업데이트하려는데_데이터가_없음_테스트() {
//        //given
//        String updateContent = "수정 #인스타 #수정";
//        Feed feed = FeedFixtureGenerator.makeOneFeedAndThreeMedia(feedService, userId);
//        when(authentication.getPrincipal()).thenReturn(userId);
//        FeedUpdateRequest feedUpdateRequest = FeedUpdateRequest.builder()
//                .feedId(feed.getId())
//                .content(updateContent)
//                .build();
//        //when
//        ResponseEntity<Feed> response = feedApi.updateMyFeed(feedUpdateRequest, authentication);
//        //then
//        assertThat(Objects.requireNonNull(response.getBody()).getContent()).isEqualTo(updateContent);
//        assertThat(response.getBody().getFeedMediaList()).isNotNull();
//        assertThat(response.getBody().getId()).isEqualTo(feed.getId());
//    }
}
