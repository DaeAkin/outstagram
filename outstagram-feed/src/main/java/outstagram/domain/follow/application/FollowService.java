package outstagram.domain.follow.application;

import outstagram.domain.follow.dto.FollowListResponse;
import reactor.core.publisher.Mono;

public interface FollowService {

    Mono<Void> followOrUnFollow(Long followingId, Long followedId);
    Mono<FollowListResponse> getFollowedList(Long userId);
    Mono<FollowListResponse> getFollowingList(Long userId);
}
