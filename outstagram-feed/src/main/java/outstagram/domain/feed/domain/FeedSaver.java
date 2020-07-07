package outstagram.domain.feed.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;

@Slf4j
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FeedSaver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "feed_subscriber")
    private User feedSubscriber;

    @OneToOne
    @JoinColumn(name = "feed_publisher")
    private User feedPublisher;

    @OneToOne
    @JoinColumn(name = "feed")
    private Feed feed;


}
