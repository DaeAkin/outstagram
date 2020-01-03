package outstagram;//import com.project.outstagram.outstagram.global.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;

//import org.springframework.security.oauth2.client.OAuth2RestTemplate;


@SpringBootApplication
//@EnableJpaAuditing
//@EnableJpaRepositories
//@RefreshScope
//@EnableEurekaClient
//@EnableCircuitBreaker
//@EnableResourceServer
// 스프링 클라우드 스트림에 애플리케이션을 메세지 브로커로 바인딩하라고 알린다.
//@EnableBinding(Source.class)
public class OutstagramInfraServerApplication {



    public static void main(String[] args) {
        SpringApplication.run(OutstagramInfraServerApplication.class, args);
    }

}
