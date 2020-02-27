package outstagram.domain.feed.application;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;
import outstagram.test.IntegrationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@Slf4j
public class FeedApi extends IntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    //https://www.baeldung.com/introduction-to-wiremock
    //http://wiremock.org/docs/stubbing//
    //https://gigsterous.github.io/engineering/2017/05/18/wiremock-testing-security.html
    //wiremock의 get메소드가 mockMvc get메소드하고 충돌
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void test() throws Exception {

        stubFor(get("/user")
                .willReturn(okJson("{\n" +
                        "    \"user\": \"1\",\n" +
                        "    \"authorities\": [\n" +
                        "        \"ROLE_USER\"\n" +
                        "    ]\n" +
                        "}")));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer xyz123");

        final ResponseEntity<String> response =
                testRestTemplate.exchange("/feed/test", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        log.info("response : {}", response.toString());

//        assertThat(response.getStatusCode(), is(HttpStatus.OK));

    }


}
