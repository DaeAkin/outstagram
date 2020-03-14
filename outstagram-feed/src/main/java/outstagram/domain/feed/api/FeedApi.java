package outstagram.domain.feed.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.application.FeedService;
import outstagram.domain.feed.dto.FeedSaveRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedApi {

    private final FeedService feedService;
    //feed 가져오기..

    //내 feed 보기..

    //feed 작성하기
    //(해시태그 분석기능?)
    // 사진,영상 화질저하 기능
    @PostMapping
    public ResponseEntity<Void> saveMyFeed(@Valid FeedSaveRequest feedSaveRequest,
                                           @RequestParam(value = "mediaFile", required = false) List<MultipartFile> mediaFile,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        feedService.saveFeed(feedSaveRequest,mediaFile,userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{feed_id}")
    public ResponseEntity<Void> deleteMyFeed(Authentication authentication, @PathVariable Long feed_id) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        feedService.deleteFeed(userId,feed_id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/test")
    public void test(Authentication authentication) {
        System.out.println("auth : ? " + authentication);
        Object principal = authentication.getPrincipal();
        System.out.println(principal);

    }

}
