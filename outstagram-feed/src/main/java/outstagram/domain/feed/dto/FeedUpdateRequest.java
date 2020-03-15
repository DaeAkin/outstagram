package outstagram.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import outstagram.domain.feed.domain.Feed;

import java.util.ArrayList;

@Data
@Slf4j
@AllArgsConstructor
@Builder
public class FeedUpdateRequest {

    Long feedId;

    String content;

    public Feed toEntity(Long userId) {
        return Feed.builder()
                .content(content)
                .userId(userId)
                .feedMediaList(new ArrayList<>())
                .build();
    }


}
