package outstagram.domain.follow.application;

import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.domain.follow.dto.FollowListResponse;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void followOrUnFollow(Long followingId, Long followedId) {
       Optional.of(isFollowed(followingId,followedId))
                .map(r -> {
                    if(r) {
                        log.debug("follow");
                         unFollow(followingId,followedId);
                    } else {
                        log.debug("unFollow");
                         follow(followingId,followedId);
                    }
                    return null;
                });
    }

    @Transactional
    public List<FollowListResponse> getFollowedList(Long userId) {
//        followRepository.findAllByFollowedId(userId)
//                .orElseGet((Supplier<? extends List<Follow>>) new ArrayList<FollowListResponse>())
//                .stream()
//                .flatMap( r -> r.)

        return null;
    }

    @Transactional
    public List<FollowListResponse> getFollowingList(Long userId) {
        return null;
    }


    private Boolean isFollowed(Long followingId, Long followedId) {
        return followRepository.findByFollowingIdAndFollowedId(followingId, followedId)
                .map(f -> true)
                .orElse(false);
    }

    private void follow(Long followingId, Long followedId) {
        Follow data = new Follow(followingId, followedId);
        followRepository.save(data)
                .pushToBeFollowed();
    }

    private void unFollow(Long followingId, Long followedId) {
        followRepository.deleteByFollowingIdAndFollowedId(followingId,followedId);

    }
}
