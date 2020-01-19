package outstagram.domain.follow.application;

import outstagram.domain.follow.dto.FollowListResponse;

import java.util.List;

public interface FollowService {

    void followOrUnFollow(Long followingId, Long followedId);
    List<FollowListResponse> getFollowedList(Long userId);
    List<FollowListResponse> getFollowingList(Long userId);
}
