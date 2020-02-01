package outstagram.domain.follow.application;

import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import outstagram.domain.follow.dto.FollowListResponse;
import outstagram.global.client.LoginRestTemplate;
import outstagram.global.error.NoDataException;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final LoginRestTemplate loginRestTemplate;

    @Transactional
    @Override
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
    @Override
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
    @Override
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

    @Transactional
    @Override
    public boolean acceptFollow(Long userId,Long f_id) {


      return  followRepository.findByFollowingIdAndFollowedId(userId,f_id)
                .map( f -> {
                        f.setFollowAccept(true);
                        followRepository.save(f);
                        return true;
                    })
              .orElse(false);
      //orElse 동작보단 Throw가 더 어울려보이는듯..
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
