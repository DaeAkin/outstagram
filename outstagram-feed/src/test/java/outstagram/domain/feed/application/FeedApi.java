package outstagram.domain.feed.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import outstagram.test.IntegrationTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Slf4j
public class FeedApi extends IntegrationTest {
//
//    @Autowired
//    TestRestTemplate testRestTemplate;

//    @MockBean
//    Authentication authentication;

    //https://www.baeldung.com/introduction-to-wiremock
    //http://wiremock.org/docs/stubbing//
    //https://gigsterous.github.io/engineering/2017/05/18/wiremock-testing-security.html
    //wiremock의 get메소드가 mockMvc get메소드하고 충돌


    @Test
    @WithMockUser(username = "mobileclient", password = "pwd", roles = "USER")
    public void test() throws Exception {
//        given(authentication.getPrincipal()).willReturn(5);

        MvcResult mvcResult = mvc
                .perform(get("/feed/test"))
                .andExpect(status().isOk())
                .andReturn();

//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer xyz123");
//
//        final ResponseEntity<String> response =
//                testRestTemplate.exchange("/feed/test", HttpMethod.GET, new HttpEntity<>(headers), String.class);
//        log.info("response : {}", response.toString());
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));

    }


}
