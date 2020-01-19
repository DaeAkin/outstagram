package outstagram.domain.follow.api;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.ResultActions;
import outstagram.domain.follow.application.FollowService;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.test.IntegrationTest;
import outstagram.test_fixture.FollowFixtureGenerator;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FollowApiTest extends IntegrationTest {



    @Autowired
    private
    FollowApi followApi;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;


    @MockBean
    Authentication authentication;


    // 팔로우 하는사람
    private final long followingId = 5;
    // 팔로우 당하는 사람
    private final long followedId = 1;

    @Test
    public void 팔로우하기() throws Exception {
        //given
        given(authentication.getPrincipal()).willReturn(followingId);
        System.out.println("test 아우띠 :" + authentication);
        given(authentication.getPrincipal()).willReturn(followingId);

        //when
        followApi.follow(followedId,authentication);
//        ResultActions follow = follow(followedId);
        Follow followed = followRepository.findByFollowingIdAndFollowedId(followingId, followedId).get();

        //then

//                .andExpect(status().isCreated());
        assertThat(followed.getFollowAccept()).isFalse();
        assertThat(followed.getFollowedId()).isEqualTo(followedId);
        assertThat(followed.getFollowingId()).isEqualTo(followingId);
    }

    @Test
    public void 언팔로우하기() {
        //given
        given(authentication.getPrincipal()).willReturn(followingId);

        //when
        followApi.follow(followedId,authentication);
        followApi.follow(followedId,authentication);
        Optional<Follow> optionalFollow = followRepository.findByFollowingIdAndFollowedId(followingId, followedId);


        //then
        assertThat(optionalFollow.isPresent()).isFalse();
    }

//    @Test
//    public void 나를팔로우한사람들_리스트_가져오기() {
//        //given
//        FollowFixtureGenerator.insertFollowingUser(followedId,followRepository,5);
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
    private ResultActions follow(Long followedId) throws Exception {
        return mvc.perform(get("/follow/"+followedId)
                .contentType(MediaType.APPLICATION_JSON).with(user("donghyeon").roles("web"))
                 )       .andDo(print());

    }

//    private EntityExchangeResult unFollow(Long followingId, Long followedId) {
//        Follow alreadyFollow = new Follow(followingId, followedId);
//        followRepository.save(alreadyFollow);
//
//        return client.get()
//                .uri("/follow/" + followedId)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectBody().isEmpty();
//    }
//
//    // 나를 팔로우 하는사람들 목록을 불러오는~
//    private EntityExchangeResult getFollowedList() {
//        return client.get()
//                .uri("/followed-list")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(FollowListResponse.class)
//                .returnResult();
//
//    }
//
//    private EntityExchangeResult getFollowingList() {
//        return client.get()
//                .uri("/following-list")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(FollowListResponse.class)
//                .returnResult();
//
//    }

}
