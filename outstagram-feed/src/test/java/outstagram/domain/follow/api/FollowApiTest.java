package outstagram.domain.follow.api;


import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import outstagram.domain.follow.application.FollowService;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.test.IntegrationTest;
import outstagram.test_fixture.FollowFixtureGenerator;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;

public class FollowApiTest extends IntegrationTest {


    @Autowired
    private
    FollowApi followApi;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    protected WebTestClient client;

    @Mock
    Authentication authentication;


    // 팔로우 하는사람
    private final long followingId = 5;
    // 팔로우 당하는 사람
    private final long followedId = 1;

    @Test
    public void 팔로우하기() {
        //given
        given(authentication.getPrincipal()).willReturn(followingId);

        //when
        follow(followedId);
        Follow followed = followRepository.findByFollowingIdAndFollowedId(followingId, followedId).block();

        //then
        assertThat(followed.getFollowAccept()).isFalse();
        assertThat(followed.getFollowedId()).isEqualTo(followedId);
        assertThat(followed.getFollowingId()).isEqualTo(followingId);
    }

    @Test
    public void 언팔로우하기() {
        //given
        given(authentication.getPrincipal()).willReturn(followingId);

        //when
        unFollow(followingId,followedId);
        Follow followed = followRepository.findByFollowingIdAndFollowedId(followingId, followedId).block();

        //then
        assertThat(followed).isNull();
    }

//    @Test
//    public void 나를팔로우한사람들_리스트_가져오기() {
//        //prepare db 필요할듯..
//        FollowFixtureGenerator.insertFollowingUser(followedId,followRepository,5);
//        //given
//        given(authentication.getPrincipal()).willReturn(followingId);
//
//        //when
//        EntityExchangeResult followedList = getFollowedList();
//
//
//
//    }
//
//    @Test
//    public void 팔로잉리스트_가져오기() {
//        //given
//        given(authentication.getPrincipal()).willReturn(followingId);
//
//        //when
//        getFollowingList();
//    }


    //setUp

    //requests~
//https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/
    private EntityExchangeResult follow(Long followedId) {
        return client.get()
                .uri("/follow/" + followedId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    private EntityExchangeResult unFollow(Long followingId, Long followedId) {
        Follow alreadyFollow = new Follow(followingId, followedId);
        followRepository.save(alreadyFollow);

        return client.get()
                .uri("/follow/" + followedId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    // 나를 팔로우 하는사람들 목록을 불러오는~
    private EntityExchangeResult getFollowedList() {
        return client.get()
                .uri("/followed-list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FollowListResponse.class)
                .returnResult();

    }

    private EntityExchangeResult getFollowingList() {
        return client.get()
                .uri("/following-list")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FollowListResponse.class)
                .returnResult();

    }

}
