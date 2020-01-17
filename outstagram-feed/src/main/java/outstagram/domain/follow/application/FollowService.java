package outstagram.domain.follow.application;

import outstagram.domain.follow.dto.FollowListResponse;
import reactor.core.publisher.Mono;

public interface FollowService {

    void followOrUnFollow(Long followingId, Long followedId);
    FollowListResponse getFollowedList(Long userId);
    FollowListResponse getFollowingList(Long userId);
}
