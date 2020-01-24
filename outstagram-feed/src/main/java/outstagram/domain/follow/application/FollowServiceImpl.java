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
import outstagram.global.client.LoginRestTemplate;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    LoginRestTemplate loginRestTemplate;

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
        return followRepository.findAllByFollowedId(userId)
                .orElseGet(ArrayList::new)
                .stream()
                .map( r -> {
                   return FollowListResponse.builder()
                           .id(r.getFollowingId())
                           .email(loginRestTemplate.getUserById(r.getFollowingId()).getEmail())
                           .build();
                         })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FollowListResponse> getFollowingList(Long userId) {
       return followRepository.findAllByFollowingId(userId)
                .orElseGet(ArrayList::new)
                .stream()
                .map( r -> {
                    return FollowListResponse.builder()
                            .id(r.getFollowedId())
                            .email(loginRestTemplate.getUserById(r.getFollowedId()).getEmail())
                            .build();
                })
                .collect(Collectors.toList());

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
