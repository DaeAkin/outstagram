package outstagram.test_fixture;

import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Random;

public class FollowFixtureGenerator {

    /**
     * @param followedId 나를 팔로우 하는사람.
     * @param followRepository
     * @param num
     * @return
     */
    public static Flux<Follow> insertFollowingUser(Long followedId, FollowRepository followRepository, Integer num) {
        Random random = new Random();
        ArrayList<Follow> followingList = new ArrayList<>();
        for(Integer i = 0; i<num; i++) {
            long followingId = (random.nextInt(1000));
            followingList.add(new Follow(followingId,followedId));
        }
        return followRepository.saveAll(followingList);
    }
}
