package com.project.outstagram;


import com.project.outstagram.global.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy // 이 서비스를 주울 서버로 사용한다.
/**
 * 이 어노테이션을 사용하면 주울 리버스 프록시필터를 로드하지 않은 주울서버나
 * 넷플릭스 유레카를 사용하는 주울 서버를 생성할 수 있다.
 * 유레카가 아닌 다른 디스커버리 엔진(콘설)과 통합할 경우 사용한다고 함.
 */
//@EnableZuulServer
//@RefreshScope
public class OutstagramZuulServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutstagramZuulServerApplication.class, args);
    }


    @LoadBalanced //RestTemplate 객체가 리본을 사용할 것을 알려줌
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if(interceptors == null) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }
}
