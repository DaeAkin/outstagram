package outstagram.global.client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private long id;

    private String email;

}