package com.project.outstagram;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TestService {

    @HystrixCommand(fallbackMethod = "iamFallback",
            threadPoolKey = "someStringThreadPool", // 스레드 풀의 고유이름 정의
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize", value = "30"), // 스레드 풀의 스레드 개수 정의
                            @HystrixProperty(name = "maxQueueSize", value = "10") // 스레드 풀 앞에 배치할 큐와 큐에 넣을 요청 수 정의
                    },
            //회로 차단기 동작 구성
            commandProperties = {
                    //호출 차단을 고려하는 데 필요한 10초 시간대 동안 연속 호출 횟수를 제어
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value="10"),
                    //회로 차단기를 차단하고 나서 지정한 값 만큼 호출한 후 타임아웃이나 예외 발생 (HTTP 500 등)
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="75"),
                    //차단되고 나서 히스트릭스가 서비스의 회복 상태를 확인할 때 까지 대기할 시간 간격
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="7000"),
                    //서비스 호출 문제를 모니터할 시간 간격을 설정 기본값은 1만 밀리초로 10초이다.
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds",value="15000"),
                    //설정한 시간 간격동안 통계를 수집할 횟수 (실패 여부)
                    @HystrixProperty(name="metrics.rollingStats.numBuckets",value="5")
            }
    )
    public String getSomeString() {
        randomlyRunLong();
        return hi();
    }

    private String hi() {
        return "hi";
    }

    private String iamFallback() {
        return "iamFallback";
    }

    private void randomlyRunLong() {
        Random rand = new Random();

        int randomNum = rand.nextInt((3 - 1) + 1 + 1);

        if (randomNum == 3) sleep();
    }

    private void sleep() {
        try {
            System.out.println("sleep()");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
