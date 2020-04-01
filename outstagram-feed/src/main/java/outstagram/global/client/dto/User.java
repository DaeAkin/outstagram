package outstagram.global.client.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private long id;

    private String email;

    private boolean privateAccount;

    public User(String email) {
        this.email = email;
    }
}
