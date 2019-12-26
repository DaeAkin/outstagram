package com.project.outstagram.global.security;

import com.project.outstagram.global.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class JWTTokenStoreConfig {

    @Autowired
    private ServiceConfig serviceConfig;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    /**
     * @Primary 어노테이션은 특정 타입의 빈이 둘 이상일 때(DefaultTokenServices)
     * @Primary로 표시된 타입을 자동 주입하도록 스프링에 설정하는 역할을 한다.
     */
    @Primary
    //서비스에 전달된 토큰에서 데이터를 읽는 데 사용한다.
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    //JWT와 OAuth2 서버 사이의 변환기로 동작한다.
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //토큰 서명에 사용되는 서명 키를 정의
        converter.setSigningKey(serviceConfig.getJwtSigningKey());;
        return converter;
    }

//    @Bean
//    public TokenEnhancer jwtTokenEnhancer() {
//        return new JWTToken
//    }
}
