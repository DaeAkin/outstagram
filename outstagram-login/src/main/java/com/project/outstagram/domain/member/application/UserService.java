package com.project.outstagram.domain.member.application;


import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.project.outstagram.domain.member.dao.UserRepository;
import com.project.outstagram.domain.member.domain.User;
import com.project.outstagram.domain.member.dto.EmailValidationResponse;
import com.project.outstagram.domain.member.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Spring-jdbc는 기본적으로 blocking 이기 때문에,
 * Stream 들이 모두 blocking 될 수 있다.
 * non-blocking 만들어 주기 위해 jdbc만 따로 스레드를 만드는 것이다.
 *
 * Conumser가 느린경우 -> ex) get,find.. etc  -> subscribeOn으로 스레드 생성. 아무데서나 체인 엮어도 됨.
 * Publisher가 느린 경우 -> ex) save ... etc -> publishOn 밑에 있는 것들만 스레드 생성
 */

@Service
@RequiredArgsConstructor
@Slf4j
@DefaultProperties(
        commandProperties = {
                @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="700"), // 0.7초 안에 응답안하면 실패처리
                @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"), //히스트릭스가 호출 차단을 고려하는 데 필요한 시간
                @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"), // 회로 차단기를 차단하고 나서 requestVolumeThreshold 값 만큼 호출한 후 타임아웃이나 예외 발생, HTTP500 반환등으로 실패해야 하는 호출 비율
                @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"), // 차단되고 나서 히스트릭스가 서비스 회복 상태를 확인할 때 까지 대기할 시간 간격
                @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"), // 히스트릭스가 서비스 호출 문제를 모니터할 시간 간격을 설정하는데 사용 기본값은 1만 밀리초, 즉 10초
                @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")})//설정한 시간 간격 동안 통계를 수집할 횟수를 설정 timeInMilliseconds와 나눴을 때 나머지가 0으로 균등하게 분할해야함. 15초 동안 5초길이로 하니까 3개의 통계 데이터가 나옴 .
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder userPasswordEncoder;



    public Mono<EmailValidationResponse> emailValidation(String email) {
        return  Mono
                .defer(() -> Mono.just(userRepository.findByEmail(email)))
                .subscribeOn(Schedulers.elastic())
                // 가져오는게 느린것들(Consumer가 느림)은 subscribeOn을 써야하는데 위치는 상관없음!
                .thenReturn(new EmailValidationResponse(true))
                .onErrorReturn(new EmailValidationResponse(false));
    }

    public Mono<Void> joinUser(UserJoinRequest userJoinRequest) {
        return Mono.just(userJoinRequest)
                .publishOn(Schedulers.elastic())
                //저장하는 작업이 느린것들(publishOn이 느린 것)밑에다 써야함
                .doOnNext( u -> {
                    User user = u.toEntity();
                    user.initialize(userPasswordEncoder);
                    userRepository.save(user);
                })
                .then().log();


    }

    @HystrixCommand(fallbackMethod = "getUserInfoFallback",
            threadPoolKey = "whoamiThreadPool", // threadPool의 이름을 설정
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize",value="30"), //스레드 풀의 개수를 설정
                            @HystrixProperty(name="maxQueueSize", value="10")})
    public Mono<User> getUserInfo(Long id) {
        sleep();
        return Mono.
                defer(() -> Mono.just(userRepository.findById(id).get()))
                .subscribeOn(Schedulers.elastic());
    }

    private Mono<User> getUserInfoFallback(Long id) {
        return Mono.just(new User("donghyeon"));
    }

    private void sleep() {
        try {
            System.out.println("sleep()");
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
