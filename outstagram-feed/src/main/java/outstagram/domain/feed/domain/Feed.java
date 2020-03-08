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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long userId;

    @OneToMany(mappedBy = "feed")
    private List<FeedMedia> feedMediaList = new ArrayList<>();

    public void analysisContentByHashTags(RestTemplate restTemplate,String content) {
        log.info("-- 분석 서버 실행 --");
        List<String> hashTags = new LinkedList<>();

    }

    private List<String> getHashTags(String content) {
        List<String> hashTags = new LinkedList<>();
        int position = content.indexOf('#');
//        while(true) {
//
//        }
    }

}
