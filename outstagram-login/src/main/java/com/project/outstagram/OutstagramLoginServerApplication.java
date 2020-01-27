package com.project.outstagram;


import brave.sampler.Sampler;
import com.project.outstagram.global.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@RefreshScope
@EnableEurekaClient
@EnableCircuitBreaker
@EnableResourceServer

//kafka https://yfkwon.tistory.com/26
@EnableBinding(Sink.class)
public class OutstagramLoginServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutstagramLoginServerApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    /** 메소드이름이 handle로 시작해야 작동함 */
    public void handle(String s) {
        System.out.println("왜 안와!!");
        System.out.println("카프카에서 보낸 메세지 : " + s);
    }
    @Bean
    public Sampler defaultSampler() {
        // return new AlwaysSampler();
        return Sampler.ALWAYS_SAMPLE;
    }

    //TODO WebFlux로 교체 예정
//    @LoadBalanced
//    @Bean
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }

    @Bean
    public OAuth2RestTemplate restTemplate(UserInfoRestTemplateFactory factory) {
        return factory.getUserInfoRestTemplate();
    }

    @Primary
    @Bean
    public RestTemplate getCustomRestTemplate() {
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();

        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }
}
