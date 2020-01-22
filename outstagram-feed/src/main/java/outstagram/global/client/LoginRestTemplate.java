package outstagram.global.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import outstagram.global.client.dto.User;

@Component
public class LoginRestTemplate {

    @Autowired private RestTemplate restTemplate;

    public User getUserById(Long userId) {

        User user = null;

        ResponseEntity<User> restExchange = restTemplate.exchange(
                "http://172.30.1.26:5555/api/loginservice/user/whoami",
                HttpMethod.GET,
                null, User.class, userId);

        user = restExchange.getBody();

        return user;
    }

}
