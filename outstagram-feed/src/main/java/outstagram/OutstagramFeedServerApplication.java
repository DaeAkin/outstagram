package outstagram;


import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
//@RefreshScope
//@EnableEurekaClient
//@EnableCircuitBreaker
//@EnableResourceServer

// 스프링 클라우드 스트림에 애플리케이션을 메세지 브로커로 바인딩하라고 알린다.
//@EnableBinding(Source.class)
public class OutstagramFeedServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutstagramFeedServerApplication.class, args);
    }

    @Bean
    public Sampler defaultSampler() {
        // return new AlwaysSampler();
        return Sampler.ALWAYS_SAMPLE;
    }





    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}
