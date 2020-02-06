package outstagram.domain.feed.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import outstagram.domain.feed.domain.Feed;

@Data
@Slf4j
public class FeedSaveRequest {
    String content;


    public Feed toEntity() {
        return null;
    }


}
