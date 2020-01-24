package outstagram.domain.follow.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import outstagram.domain.follow.application.FollowService;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.global.client.LoginRestTemplate;

import java.util.List;

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


}
