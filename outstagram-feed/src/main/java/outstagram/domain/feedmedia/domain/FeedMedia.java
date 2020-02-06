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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;
}
