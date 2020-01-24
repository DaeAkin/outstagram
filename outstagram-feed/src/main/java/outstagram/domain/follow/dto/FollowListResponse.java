package outstagram.domain.follow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowListResponse {

    long id;
    String email;
}
