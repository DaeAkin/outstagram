package outstagram.domain.follow.application;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.domain.follow.dto.FollowListResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private FollowRepository followRepository;
    private WebClient webClient;

    public void followOrUnFollow(Long followingId, Long followedId) {
       Optional.of(isFollowed(followingId,followedId))
                .map(r -> {
                    if(r) {
                         unFollow(followingId,followedId);
                    } else {
                         follow(followingId,followedId);
                    }
                    return null;
                });
    }

    public FollowListResponse getFollowedList(Long userId) {
        return null;
    }

    public FollowListResponse getFollowingList(Long userId) {
        return null;
    }


    private Boolean isFollowed(Long followingId, Long followedId) {
        return followRepository.findByFollowingIdAndFollowedId(followingId, followedId)
                .map(f -> true)
                .orElse(false);
    }

    private void follow(Long followingId, Long followedId) {
        Follow data = new Follow(followingId, followedId);
        followRepository.save(data)
                .pushToBeFollowed();
    }

    private Mono<Void> unFollow(Long followingId, Long followedId) {

//        Mono.empty()
//        return Mono
//                .defer(() -> followRepository.deleteAll())
////                .defer(() -> Mono.just(followRepository.deleteByFollowingIdAndFollowedId(followingId,followedId)))
//                .subscribeOn()
//                .then()
//                .log();
return null;
    }
}
