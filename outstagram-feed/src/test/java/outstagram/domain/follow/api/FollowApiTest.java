package outstagram.domain.follow.api;


import com.netflix.discovery.converters.Auto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.ResultActions;
import outstagram.domain.follow.application.FollowService;
import outstagram.test.IntegrationTest;
import reactor.core.publisher.Mono;


public class FollowApiTest extends IntegrationTest {


    @Autowired
    private
    FollowApi followApi;

    @Autowired
    private
    FollowService followService;

    @Autowired
    protected WebTestClient client;





    private final long followingId = 5;
    private final long followedId = 1;

    @Test
    public void 팔로우하기() {
    System.out.println(followService);
        //when
        follow();
        System.out.println(followApi);






    }

    @Test
    public void 언팔로우하기() {

    }

    private EntityExchangeResult follow() {
        return  client.get()
                .uri("/follow/" + followedId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

}
