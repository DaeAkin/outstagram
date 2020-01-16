package outstagram;


import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableMongoAuditing
@EnableReactiveMongoRepositories
@RefreshScope
@EnableEurekaClient
@EnableCircuitBreaker
@EnableResourceServer
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
    WebClient webClient(){
        return WebClient.builder()
//                .filter(new LoadBalancerExchangeFilterFunction(loadBalancerClient))
                .build();
    }


    //TODO WebFlux로 교체 예정
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}
