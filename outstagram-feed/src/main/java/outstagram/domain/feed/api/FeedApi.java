package outstagram.domain.feed.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.application.FeedService;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedSaveRequest;
import outstagram.domain.feed.dto.FeedUpdateRequest;
import outstagram.global.exception.NoDataException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedApi {

    private final FeedService feedService;

    //계정 눌렀을 때 정보 들어가지는 API
    @GetMapping("/feed-list/{user_id}")
    public ResponseEntity getFeedList(@PathVariable Long user_id) {
        return null;
    }
    //feed 가져오기..
    @GetMapping("/{feed_id}")
    public ResponseEntity<Feed> getFeedByFeedId(@PathVariable Long feed_id,Authentication authentication) {
        //이 친구랑 Follow 됐는지 확인해야함. 안되면 권한없음. 2XX 응답
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        Feed feed = feedService.getFeedByFeedId(userId, feed_id);

        return new ResponseEntity<>(feed,HttpStatus.OK);
    }
    //feed 작성하기
    //(해시태그 분석기능?)
    // 사진,영상 화질저하 기능
    @PostMapping
    public ResponseEntity<Feed> saveMyFeed(@Valid FeedSaveRequest feedSaveRequest,
                                           @RequestParam(value = "mediaFile", required = false) List<MultipartFile> mediaFile,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        Feed feed = feedService.saveFeed(feedSaveRequest, mediaFile, userId);
        return new ResponseEntity<>(feed,HttpStatus.CREATED);
    }

    //수정은 content만 가능
    @PatchMapping("/{feed_id}")
    public ResponseEntity<Feed> updateMyFeed(@RequestBody @Valid FeedUpdateRequest feedUpdateRequest, Authentication authentication, @PathVariable Long feed_id) {
        System.out.println("update");
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        Feed feed = feedService.updateFeed(feedUpdateRequest, userId,feed_id);
        return new ResponseEntity<>(feed,HttpStatus.OK);
    }

    @DeleteMapping("/{feed_id}")
    public ResponseEntity<Void> deleteMyFeed(Authentication authentication, @PathVariable Long feed_id) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        feedService.deleteFeed(userId,feed_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/test")
    public String test(Principal principal) {
        System.out.println(principal);
        String aa = principal.getName();
        System.out.println("----- userId -----");
        System.out.println("userId : " + aa);
//        System.out.println("auth : ? " + authentication);
        return aa;
    }



}
