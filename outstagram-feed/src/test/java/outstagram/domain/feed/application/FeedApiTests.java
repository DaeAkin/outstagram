package outstagram.domain.feed.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.api.FeedApi;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.test.IntegrationTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
public class FeedApiTests extends IntegrationTest {
//
//    @Autowired
//    TestRestTemplate testRestTemplate;

    @Autowired
    FeedApi feedApi;
    @MockBean
    Authentication authentication;

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
        when(authentication.getPrincipal()).thenReturn(5L);
        //when
        feedApi.saveMyFeed(feedSaveRequest,givenFileList,authentication);
        //then

    }



}
