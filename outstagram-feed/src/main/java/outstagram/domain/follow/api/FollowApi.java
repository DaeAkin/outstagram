package outstagram.domain.follow.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import outstagram.domain.follow.application.FollowService;
import outstagram.domain.follow.application.FollowServiceImpl;
import outstagram.domain.follow.dto.FollowListResponse;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
/**
 * TODO : 팔로우시 푸시메세지 날리는건 카프카 이용해야할듯.
 */
@RequestMapping("/follow")
public class FollowApi {

    @Autowired
    FollowService followService;

    @ResponseStatus(value = HttpStatus.CREATED)
    // follow or unFollow
    @GetMapping("/{userId}")
    public Mono<Void> follow(@PathVariable("userId") Long userId, Authentication authentication) {
        Long id = Long.parseLong(authentication.getPrincipal().toString());

        return null;
    }

    // get followed list
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/followed-list")
    public Mono<FollowListResponse> followedList(Authentication authentication) {
        return null;
    }

    // get following list
    @GetMapping("/following-list")
    public Mono<FollowListResponse> followingList(Authentication authentication) {
        return null;
    }


}
