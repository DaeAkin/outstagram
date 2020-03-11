package outstagram.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import outstagram.domain.feed.domain.Feed;
import outstagram.domain.feedmedia.domain.FeedMedia;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
public class FeedSaveRequest {
    String content;


    public Feed toEntity(Long userId) {
        return Feed.builder()
                .content(content)
                .userId(userId)
                .feedMediaList(new ArrayList<>())
                .build();
    }

}
