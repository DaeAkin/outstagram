package outstagram.domain.follow.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * MSA 아키텍처라서 USER랑 연관관계 매핑이 안될거 같음.
     */
    // 팔로우할 사람 userId
    private Long followingId;

    //그 사람을 팔로우한 사람의 userId
    private Long followedId;

    private boolean accept = false;


}
