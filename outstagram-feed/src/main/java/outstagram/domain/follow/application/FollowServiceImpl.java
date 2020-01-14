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

import java.util.function.Supplier;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private  FollowRepository followRepository;
    private  WebClient webClient;

    public Mono<Void> followOrUnFollow(Long followingId, Long followedId) {

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
                .defer(() -> Mono.just(followRepository.findByFollowingIdAndFollowedId(followingId, followedId)))
                .subscribeOn(Schedulers.elastic())
                .thenReturn(true)
                .onErrorReturn(false)
                .log();

    }

    private Mono<Void> follow(Long followingId, Long followedId) {
        Follow data = new Follow(followingId, followedId);

        return Mono.just(followRepository.save(data))
                .publishOn(Schedulers.elastic())
                .doOnSuccess( f -> f.pushToBeFollowed())
                .then()
                .log();

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
