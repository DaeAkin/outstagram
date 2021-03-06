package outstagram.domain.follow.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.global.domain.BaseAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Slf4j
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Follow extends BaseAuditingEntity {

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
    @Setter
    // boolean -> Boolean 변경 .. 왜 primitive 타입 못쓰지?
    private Boolean followAccept = false;

    //cons


    public Follow(Long followingId, Long followedId) {
        this.followingId = followingId;
        this.followedId = followedId;
    }

    public void pushToBeFollowed() {
        // 푸쉬알림 추후작성예정.
    }

//    public toFollowListResponse() {
//        return FollowListResponse
//    }
}
