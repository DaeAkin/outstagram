package outstagram.domain.feed.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.api.FeedApi;
import outstagram.domain.feed.application.FeedService;
import outstagram.domain.feed.dao.FeedRepository;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.domain.feed.dto.FeedUpdateRequest;
import outstagram.domain.feedmedia.dao.FeedMediaRepository;
import outstagram.domain.feedmedia.domain.FeedMedia;
import outstagram.global.exception.NoDataException;
import outstagram.test.IntegrationTest;
import outstagram.test_fixture.FeedFixtureGenerator;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class FeedApiTests extends IntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

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
//    @Test
//    public void 피드_작성하기_테스트() {
//        //given
//        String content = "#인스타 안녕하세요 인스타그램 입니다. 반가워요" +
//                "#인스타그램 #맞팔 #헤헤헤 ";
//        List<MultipartFile> givenFileList = Arrays.asList(
//                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
//                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes()),
//                new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes())
//        );
//        FeedSaveRequest feedSaveRequest = new FeedSaveRequest(content);
//        when(authentication.getPrincipal()).thenReturn(userId);
//
//        //when
//        ResponseEntity<Feed> response = feedApi.saveMyFeed(feedSaveRequest, givenFileList, authentication);
//        //then
//        Feed feed = feedRepository.findFeedByUserId(userId).get();
//        assertThat(((List<FeedMedia>) feedMediaRepository.findAll()).size()).isEqualTo(3);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(Objects.requireNonNull(response.getBody()).getUserId()).isEqualTo(userId);
//        assertThat(response.getBody().getFeedMediaList().get(0).getResourceLocation()).isNotBlank();
//    }

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

    @Test(expected = NoDataException.class)
    public void 피드_업데이트하려는데_데이터가_없음_테스트() {
        //given
        String updateContent = "수정 #인스타 #수정";
        when(authentication.getPrincipal()).thenReturn(userId);
        FeedUpdateRequest feedUpdateRequest = FeedUpdateRequest.builder()
                .feedId(3L)
                .content(updateContent)
                .build();
        //when
        ResponseEntity<Feed> response = feedApi.updateMyFeed(feedUpdateRequest, authentication);
        System.out.println("에러쓰" + response.toString());
    }

    @Test
    public void justTest() throws Exception {

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("me");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/feed/test")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

         mvc.perform(requestBuilder).andDo(print());

//        MockHttpServletResponse response = result.getResponse();
//        int status = response.getStatus();
//        Assert.assertEquals("response status is wrong", 200, status);

//        Principal principal = Mockito.mock(Principal.class);
//        System.out.println("테스트에서 principal :" +principal);
//        Mockito.when(principal.getName()).thenReturn("me");
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/feed/test")
//                .principal(principal)
//                .accept(MediaType.APPLICATION_JSON);
//
//
////        mvc.perform(get("/feed/test").principal(principal)).andExpect(status().isOk());
//        MvcResult result = mvc.perform(requestBuilder).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        int status = response.getStatus();
//        System.out.println(response.getContentAsString());

//        HttpEntity<String> request = new HttpEntity<String>(headers);
//        ResponseEntity<Feed> responseEntity = restTemplate.getForEntity("/feed/test",Feed.class);
//        System.out.println(responseEntity.toString());
    }
}