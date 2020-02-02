package outstagram.domain.follow.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import outstagram.domain.follow.application.FollowService;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.global.event.SimpleSourceBean;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/follow")
public class FollowApi {

    private final FollowService followService;
    private final SimpleSourceBean simpleSourceBean;

    @ResponseStatus(value = HttpStatus.CREATED)
    // follow or unFollow
    @GetMapping("/{userId}")
    public void follow(@PathVariable("userId") Long userId, Authentication authentication) {
        log.debug("followingId : {} , followedId : {} " ,authentication.getPrincipal(),userId );
        Long id = Long.parseLong(authentication.getPrincipal().toString());
        followService.followOrUnFollow(id,userId);
    }

    // get followed list
    @GetMapping("/followed-list")
    public ResponseEntity<List<FollowListResponse>> followedList(Authentication authentication) {
        Long id = Long.parseLong(authentication.getPrincipal().toString());
        return new ResponseEntity<>(followService.getFollowedList(id),HttpStatus.OK);
    }

    // get following list
    @GetMapping("/following-list")
    public ResponseEntity<List<FollowListResponse>> followingList(Authentication authentication) {
        Long id = Long.parseLong(authentication.getPrincipal().toString());
        return new ResponseEntity<>(followService.getFollowingList(id),HttpStatus.OK);
    }

    @PatchMapping("/accept/{following_id}")
    public ResponseEntity<Void> acceptFollow(@PathVariable("following_id") Long following_id, Authentication authentication) {
        Long id = Long.parseLong(authentication.getPrincipal().toString());
        followService.acceptFollow(id,following_id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }



    @GetMapping("/test")
    public void test() {
        System.out.println("이벤트 테스트");
        simpleSourceBean.publishFcmMessage();
    }

}
