package outstagram.domain.feed.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import outstagram.domain.feedmedia.domain.FeedMedia;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feed {
    public Feed(String content) {
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long userId;

    @Lob
    private String content;

    @OneToMany(mappedBy = "feed")
    private List<FeedMedia> feedMediaList = new ArrayList<>();

    public void analysisContentByHashTags(RestTemplate restTemplate) {
        log.info("-- 분석 서버 실행 --");
        System.out.println(getHashTags());

    }

    public List<String> getHashTags() {
        List<String> hashTags = new LinkedList<>();
        StringBuffer sb = new StringBuffer(content);
        while (true) {
            int position = sb.indexOf("#");
            int lastPosition = position;
            while (true) {
                lastPosition++;
                if (sb.length() <= lastPosition)
                    break;
                if (sb.charAt(lastPosition) == '#' || sb.charAt(lastPosition) == ' ') {
                    break;
                }
            }
            if (position == -1)
                break;
            else {
                String target = sb.substring(position, lastPosition);
                hashTags.add(target);
                sb.replace(position, lastPosition, "");

            }
        }
        return hashTags;
    }

    public FeedMedia toFeedMedia(String resourcePath) {
        return FeedMedia.builder()
                .feed(this)
                .resourceLocation(resourcePath)
                .build();
    }


}
