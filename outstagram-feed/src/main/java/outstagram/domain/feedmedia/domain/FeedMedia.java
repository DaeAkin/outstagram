package outstagram.domain.feedmedia.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import outstagram.domain.feed.domain.Feed;

import javax.persistence.*;

@Slf4j
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedMedia {

    public FeedMedia(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String resourceLocation;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public void setFeed(Feed feed) {
        this.feed = feed;
        feed.getFeedMediaList().add(this);
    }

}
