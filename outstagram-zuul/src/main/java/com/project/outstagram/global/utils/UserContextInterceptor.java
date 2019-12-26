package com.project.outstagram.global.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * RestTemplate 인스턴스에서 실행되는 모든 HTTP 기반 서비스 발신 요청에
 * 상관관계 ID를 삽입한다. 이 작업은 서비스 호출 간 연결을 형성하는 데 수행된다.
 * WebFlux는 다른걸 써야 할듯?
 * 이 인터셉터를 사용하려면 RestTemplate 빈을 정의 한 후
 * UserContextInterceptor를 그 빈에 추가해야 한다.
 * @See OutstagramZuulServerApplication.class
 */

//RestTemplate 전용 인터셉터
public class UserContextInterceptor implements ClientHttpRequestInterceptor {
    // RestTemplate으로 실제 HTTp 서비스 호출을 하기 전에 intercept() 메소드가 호출된다.
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        //서비스 호출을 위해 준비할 HTTP 요청 헤더를 가져와 UserContext에 저장된 상관관계 ID를 추가한다.
        headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());

        return execution.execute(request, body);
    }
}
