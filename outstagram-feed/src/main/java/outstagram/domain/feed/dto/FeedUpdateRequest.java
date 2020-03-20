package outstagram.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import outstagram.domain.feed.domain.Feed;

import java.util.ArrayList;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedUpdateRequest {

    String content;

    public Feed toEntity(Long userId) {
        return Feed.builder()
                .content(content)
                .userId(userId)
                .feedMediaList(new ArrayList<>())
                .build();
    }


}
