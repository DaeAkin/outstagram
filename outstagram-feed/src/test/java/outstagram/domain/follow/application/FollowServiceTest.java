package outstagram.domain.follow.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.global.client.LoginRestTemplate;
import outstagram.global.client.dto.User;
import outstagram.global.exception.NoDataException;
import outstagram.test.MockTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
@Slf4j
public class FollowServiceTest extends MockTest {

    @Mock
    FollowRepository followRepository;

    @Mock
    Authentication authentication;

    @Mock
    LoginRestTemplate loginRestTemplate;

    @InjectMocks
    FollowServiceImpl followService;


    private final long followingId = 5;
    private final long followedId = 1;

    @Test
    public void 나를팔로우한_사람들_리스트_하나도없음() {
        //given
        long userId = 5;
        given(followRepository.findAllByFollowedId(userId)).willReturn(Optional.empty());

        //when
        List<FollowListResponse> followedList = followService.getFollowedList(userId);

        //then
        assertThat(followedList).isEmpty();
    }

    @Test
    public void 나를팔로우한_사람들이_있음() {
        //given
        long userId = 5;
        List<Follow> follows = followedList(userId);
        for (Follow follow: follows) {
            given(loginRestTemplate.getUserById(follow.getFollowingId()))
                    .willReturn(new User("follow@gmail.com"));
        }
        given(followRepository.findAllByFollowedId(userId))
                .willReturn(Optional.of(follows));

        //when
        List<FollowListResponse> followedList = followService.getFollowedList(userId);

        //then
        assertThat(followedList).isNotEmpty();
        assertThat(followedList.size()).isEqualTo(3);

    }

    @Test(expected = NoDataException.class)
    public void 팔로우수락_그런데_요청한팔로우가없음_테스트() {
        //given
        boolean result = followService.acceptFollow(5L, 3L);
    }

    @Test
    public void 팔로우수락_테스트() {
        //given
        given(followRepository.findByFollowingIdAndFollowedId(5L,3L))
                .willReturn(Optional.of(new Follow(5L,3L)));

        boolean result = followService.acceptFollow(3L, 5L);

        assertThat(result).isTrue();
    }

    private List<Follow> followedList(long followedId) {
        Follow follow1 = new Follow(1L,followedId);
        Follow follow2 = new Follow(2L,followedId);
        Follow follow3 = new Follow(3L,followedId);
        List<Follow> follows = Arrays.asList(follow1, follow2, follow3);
        return follows;
    }


}
