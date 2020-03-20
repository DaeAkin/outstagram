package outstagram.domain.feed.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
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
import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

//    Principal mockPrincipal = Mockito.mock(Principal.class);


    @MockBean
    Authentication authentication;

    Long userId = 5L;

    @Test
    public void 피드_작성하기_테스트() throws Exception {
        //given
        String content = "#인스타 안녕하세요 인스타그램 입니다. 반가워요" +
                "#인스타그램 #맞팔 #헤헤헤 ";

        MockMultipartFile file1 = new MockMultipartFile("mediaFile", "test1.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("mediaFile", "test2.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("mediaFile", "test3.jpg", MediaType.IMAGE_JPEG_VALUE, "testImage".getBytes());
        when(authentication.getPrincipal()).thenReturn(userId);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/feed")
                .file(file1)
                .file(file2)
                .file(file3)
                .principal(authentication)
                .param("content", content)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions resultAction = mvc.perform(requestBuilder);

        //then
        resultAction.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId").value(userId))
                .andExpect(jsonPath("content").value(content))
                .andExpect(jsonPath("$.feedMediaList",hasSize(3)))
                .andExpect(jsonPath("$.feedMediaList[*].resourceLocation",notNullValue()))
                .andExpect(jsonPath("$.hashTags",is(Arrays.asList("#인스타","#인스타그램","#맞팔","#헤헤헤"))));
    }

    @Test
    public void 피드_삭제하기_테스트() throws Exception {
        //given
        Feed feed = FeedFixtureGenerator.makeOneFeedAndThreeMedia(feedService, userId);

        Mockito.when(authentication.getPrincipal()).thenReturn(String.valueOf(userId));

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/feed/" + feed.getId())
                .principal(authentication)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions resultAction = mvc.perform(requestBuilder);

        //then
        resultAction.andExpect(status().isNoContent());
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

//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn("me");
//
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/feed/test")
//                .principal(mockPrincipal)
//                .accept(MediaType.APPLICATION_JSON);
//
//         mvc.perform(requestBuilder).andDo(print());

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
