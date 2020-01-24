package outstagram.test_fixture;

import outstagram.domain.follow.dao.FollowRepository;
import outstagram.domain.follow.domain.Follow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FollowFixtureGenerator {

    /**
     * followedId followingId
     * followedId   random
     * followedId   random
     * @param followedId 나를 팔로우 하는사람.
     * @param followRepository
     * @param num
     * @return
     */
    public static List<Follow> insertFollowingUser(Long followedId, FollowRepository followRepository, Integer num) {
        Random random = new Random();
        ArrayList<Follow> followingList = new ArrayList<>();
        for(Integer i = 0; i<num; i++) {
            long followingId = (random.nextInt(1000));
            followingList.add(new Follow(followingId,followedId));
        }
        return (List)followRepository.saveAll(followingList);
    }

    public static List<Follow> insertFollowedUser(Long followingId, FollowRepository followRepository, Integer num) {
        Random random = new Random();
        ArrayList<Follow> followingList = new ArrayList<>();
        for(Integer i = 0; i<num; i++) {
            long followedId = (random.nextInt(1000));
            followingList.add(new Follow(followingId,followedId));
        }
        return (List)followRepository.saveAll(followingList);
    }
}
