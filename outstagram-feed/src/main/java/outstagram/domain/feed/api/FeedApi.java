package outstagram.domain.feed.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import outstagram.domain.feed.dto.FeedSaveRequest;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedApi {

    //feed 가져오기..

    //내 feed 보기..

    //feed 작성하기.. (해시태그 분석기능?)
    @PostMapping("/write")
    public ResponseEntity<Void> saveMyFeed(@Valid FeedSaveRequest feedSaveRequest,
                                           @RequestParam(value = "mediaFile", required = false) List<MultipartFile> mediaFile,
                                           Authentication authentication) {


        return null;
    }
    //feed 수정하기..

    @GetMapping("/test")
    public void test(Authentication authentication) {
        System.out.println("auth : ? " + authentication);
        Object principal = authentication.getPrincipal();
        System.out.println(principal);

    }

}
