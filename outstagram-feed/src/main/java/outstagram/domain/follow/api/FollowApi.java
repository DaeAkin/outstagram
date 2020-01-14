package outstagram.domain.follow.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import outstagram.domain.follow.application.FollowServiceImpl;
import outstagram.domain.follow.dto.FollowListResponse;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
/**
 * TODO : 팔로우시 푸시메세지 날리는건 카프카 이용해야할듯.
 */
@RequestMapping("/follow")
public class FollowApi {

    private final FollowServiceImpl followServiceImpl;

    // follow or unFollow
    @GetMapping("/{userId}")
    public Mono<Void> follow(@PathVariable("userId") Long userId, Authentication authentication) {
        Long id = Long.parseLong(authentication.getPrincipal().toString());

        return null;
    }

    // get followed list
    @GetMapping("/followed-list")
    public Mono<FollowListResponse> followedList() {
        return null;
    }

    // get following list
    @GetMapping("/following-list")
    public Mono<FollowListResponse> followingList() {
        return null;
    }


}
