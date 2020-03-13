package outstagram.domain.feed.application;

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
import outstagram.domain.feed.dao.FeedRepository;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.test.IntegrationTest;

import java.util.Arrays;
import java.util.List;

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

    @MockBean
    Authentication authentication;

    @Test
    public void 피드_작성하기_테스트() {
        //given
        Long userId = 5L;
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
        ResponseEntity<Void> response = feedApi.saveMyFeed(feedSaveRequest, givenFileList, authentication);
        //then
        Feed feed = feedRepository.findFeedByUserId(userId).get();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(feed.getFeedMediaList().size()).isEqualTo(3);
        assertThat(feed.getUserId()).isEqualTo(userId);
        assertThat(feed.getFeedMediaList().get(0).getResourceLocation()).isNotBlank();

    }



}
