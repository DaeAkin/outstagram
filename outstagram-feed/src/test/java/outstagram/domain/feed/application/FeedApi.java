package outstagram.domain.feed.application;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import outstagram.test.IntegrationTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class FeedApi extends IntegrationTest {

    @Test
    @WithMockUser
    public void test() throws Exception {
        mvc.perform(get("/feed/test")
                .contentType(MediaType.APPLICATION_JSON).with(user("donghyeon").roles("web"))
        )       .andDo(print());

    }
}
