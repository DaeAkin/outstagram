package outstagram.domain.follow.application;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.test.MockTest;

import static org.mockito.BDDMockito.given;

public class FollowServiceTest extends MockTest {

    @InjectMocks
    FollowService followService = new FollowServiceImpl();

    @Mock
    FollowRepository followRepository;

    @Mock
    Authentication authentication;

    private final long followingId = 5;
    private final long followedId = 1;

    @Test
    public void 팔로우하기() {
        //given
        given(followRepository.findByFollowingIdAndFollowedId(followedId,followingId))
                .willReturn(java.util.Optional.of(new Follow()));


        //when
        followService.followOrUnFollow(followingId,followedId);




    }

    @Test
    public void 언팔로우하기()  {

    }

}
