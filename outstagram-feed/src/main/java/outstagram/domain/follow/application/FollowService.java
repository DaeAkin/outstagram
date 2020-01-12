package outstagram.domain.follow.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.dto.FollowListResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;
    private final WebClient webClient;

    public Mono<Void> follow(Long followingId, Long followedId) {

//        isFollowed(followingId,followedId)
                
        return null;
    }

    public Mono<FollowListResponse> getFollowedList(Long userId) {
        return null;
    }

    public Mono<FollowListResponse> getFollowingList(Long userId) {
        return null;
    }

    private Mono<Boolean> isFollowed(Long followingId, Long followedId) {
        return Mono
                .defer(() -> Mono.just(followRepository.findByFollowingIdAndFollowedId(followedId, followedId)))
                .subscribeOn(Schedulers.elastic())
                .thenReturn(true)
                .onErrorReturn(false)
                .log();


    }
}
