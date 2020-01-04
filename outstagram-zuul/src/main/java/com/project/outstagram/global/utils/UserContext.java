package com.project.outstagram.global.utils;

import org.springframework.stereotype.Component;

/**
 * UserContext클래스는 마이크로서비스가
 * 처리하는 개별 서비스 클라이언트 요청에 대한 HTTP 헤더 값을 저장하는데 사용 된다.
 * 이 클래스는 정보를 저장하는 POJO 인데,
 * UserContextHolder.class가 스레드 로컬을 이용해 모든 스레드가 접근이 가능하게 만들어준다.
 */
@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "Authorization";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORG_ID         = "tmx-org-id";

    private String correlationId= new String();
    private String authToken= new String();
    private String userId = new String();
    private String orgId = new String();

    public String getCorrelationId() { return correlationId;}
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}