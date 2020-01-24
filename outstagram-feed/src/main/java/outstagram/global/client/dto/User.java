package outstagram.global.client.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private long id;

    private String email;

    public User(String email) {
        this.email = email;
    }
}
