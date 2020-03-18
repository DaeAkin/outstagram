package outstagram.domain.feed.application;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import outstagram.domain.feed.dao.FeedRepository;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feed.dto.FeedUpdateRequest;
import outstagram.global.exception.NoDataException;
import outstagram.test.MockTest;
import outstagram.test_fixture.FeedFixtureGenerator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class FeedServiceTests extends MockTest {
    @Mock
    FeedRepository feedRepository;

    @InjectMocks
    FeedService feedService;


//    @Test(expected = NoDataException.class)
//    public void 피드_업데이트하려는데_데이터가_없음_테스트() {
//        //given
//        String updateContent = "수정 #인스타 #수정";
//
//
//        FeedUpdateRequest feedUpdateRequest = FeedUpdateRequest.builder()
//                .feedId(feed.getId())
//                .content(updateContent)
//                .build();
//        //when
//        ResponseEntity<Feed> response = feedApi.updateMyFeed(feedUpdateRequest, authentication);
//    }

}
