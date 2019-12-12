# login

gradle 멀티모듈에서는 필요한 의존성을 전부 주입하지말고, 각 모듈마다 지정해주는게 좋음.
configserver 인식이 안되서 애먹었음. (login 모듈이 config-server 라이브러리를 갖고있어서 오류)



#히스트릭스


컨트롤러 내부에 메소드를 작성해서 어노테이션을 붙여줫는데, 동작을 안한다, 
서비스 클래스를 만들어서 하니까 됨.

컨트롤러는 다이나믹 프록시로 동작을 안해서 그런거같기도?

## 주의점

기본적인 프로퍼티 없이 @HystrixCommand 어노테이션을 지정하면 어노테이션은 모든 원격 서비스 호출에 동일한 스레드풀을 사용하므로 애플리케이션에서 문제를 일으킬 수 있다. (벌크헤드 패턴 구현을 해야함)



## 호출대기시간 사용자 정의

```java
@HystrixCommand(
        commandProperties = {
                @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "6000")
        }
)
public String getSomeString() {
    randomlyRunLong();

    return hi();
}
```

6초로 줌 (기본 1초) 그런데 1초이상 주지 말라고 하네요... 



# 폴백 프로세싱

회로 차단기 패턴의 장점은 원격 자원의 소비자와 리소스 사이에 "중간자"를 두어 개발자에게 서비스 실패를 가로채고 다른 대안을 선택할 기회를 준다는 것.



이것은 히스트릭스에서 폴백 전략이라고 알려져 있으며, 구현도 쉽게 할 수 있다.



```java
    @HystrixCommand( fallbackMethod = "iamFallback"})
    public String getSomeString() {
        randomlyRunLong();
        return hi();
    }

    private String hi() {
        return "hi";
    }
		//hi가 실패하면 iamFallback이 호출
		//주의 : @HystrixCommand와 같은 클래스에 있어야함.
    private String iamFallback() {
        return "iamFallback";
    }
```

폴백 전략은 마이크로서비스가 데이터를 검색한 호출이 실패하는 상호아에 매우 적합하다.

예시) 정상적일 때는 항상 최신 데이터를 가져와 바로 데이터 요약본을 추출하지만 데이터베이스 접속이 느려져 다수 서비스가 중단된 심각한 장애를 겪은 후 고객 정보를 검색하고 요약하는 서비스 호출을 히스트릭스 폴백 구현을 이용해 보호해도된다.
즉 성능 문제나 에러로 호출이 실패하면 데이터 웨어하우스 테이블에서 요약된 데이터를 ㄱ검색하는 폴백을 사용함.



## 주의점

1. 폴백은 자원이 타임아웃되거나 실패할 때 행동 방침을 제공하는 메커니즘이다. 폴백을 사용해 타임아웃 예외를 잡아내고 에러 로깅만 한다면 서비스 호출 전후로 표준 try~catch 블록을 사용하고 로깅 로직을 그 블록 안에 넣어도 된다.
2. 폴백 기능으로 수행하는 행동을 알고 있어야 한다. 폴백 서비스에서 다른 분산 서비스를 호출한다면 @HystrixCommand 어노테이션으로 폴백을 감싸야 할 수 있다. 1차 폴백 행동 방침을 겪게 한 동일한 장애가 2차 폴백 옵션에도 영향을 줄 수 있다.

# 벌크헤드

마이크로서비스 기반의 애플리케이션에서 종종 특정 작업을 완료하기 위해 여러 마이크로서비스를 호출해야 한다. 버릌헤드 패턴을 적용하지 않는다면 기본적인 호출 행위는 전체 자바 컨테이너에 대한 요청을 처리하는 스레드에서 이루어지낟. 대규모 상황에서 한 서비스에서 발생한 성능 문제로 자바 컨테이너의 모든 스레드가 최대치에 도달해 작업 처리를 대기하고, 새로운 요청들을 적재된다. 결국 자바 컨테이너는 비정상 종료한다. 벌크헤드 패턴은 원격 자원 호출을 자신의 스레드 풀에 격리하므로 오작동 서비스를 억제하고 컨테이너의 비정상 종료를 방지한다.

히스트릭스는 스레드 풀을 사용해 원격 서비스에 대한 모든 요청을 위임한다. 기본적으로 모든 히스트릭스 명령은 요청을 처리하기 위해 동일한 스레드 풀을 공유한다. 이 스레드 풀에는 원격 서비스 호출을 처리할 10개의 스레드가 있고, 원격 서비스 호출은 REST 서비스 호출, 데이터베이스 호출 등 무엇이든 될 수 있다.

## 분리된 스레드풀 구현 

1. 호출이 될 메소드 호출을 위한 별도 스레드 풀 설정하기
2. 스레드 풀의 스레드 숫자 설정하기
3. 스레드가 분주할 때 큐에 들어갈 요청 수에 해당하는 큐의 크기 설정하기

```java
@HystrixCommand(fallbackMethod = "iamFallback",
        threadPoolKey = "someStringThreadPool", // 스레드 풀의 고유이름 정의
        threadPoolProperties =
                {@HystrixProperty(name = "coreSize", value = "30"), // 스레드 풀의 스레드 개수 정의
                        @HystrixProperty(name = "maxQueueSize", value = "10") // 스레드 풀 앞에 배치할 큐와 큐에 넣을 요청 수 정의
                }
)
public String getSomeString() {
    randomlyRunLong();
    return hi();
}

```

threadPoolKey 속성은 히스트릭스에 새로운 스레드풀을 설정하라고 알린다. 이름값을 주지 않으면, 이름없이 스레드 풀을 만들지만 기본값들로 구성한다.

스레드 풀을 사용자 정의하려면 @HystrixCommand의  threadPoolProperties 속성을 이용해야 한다. 이 속성을 HystrixProperty 객체 배열에 저장하고 HystrixProperty 객체는 스레드 풀의 동작을 제어하는데 사용된다. 예를 들어 coreSize 속성으로 스레드 풀 크기를 설정할 수 있다.

스레드들이 분주할 때 스레드 풀의 앞단 요청을 백업할 큐를 만들고, 요청 수에 맞는 큐 크기를 정하기 위해 maxQueueSize 속성을 설정한다. 요청 수가 큐 크기를 초과하면 큐에 여유가 생길 때까지 스레드 풀에 대한 추가 요청은 모두 실패한다.

maxQueueSize 의 값을 -1로 설정하면 SynchronousQueue가 사용되는데, 동시기 큐를 사용하면 본질적으로 스레드 풀에서 가용한 스레드 개수보다 더 많은 요청을 처리할 수 없다.

1보다 큰 값을 설정하면 LinkedBlockingQueue를 사용한다. 이걸 사용하면 모든 스레드가 요청을 처리하는 데 분주하더라도 더 많은 요청을 큐에 넣을 수 있다.

maxQueueSize는 어플리케이션이 처음 실행됐을때만 설정할수 있지만, maxQueueSize 속성이 0보다 클 때만 이 속성을 설정할 수 있다. 

넷플릭스가 제안하는 스레드 풀의 적정 크기는 

**(서비스가 정상일 때 최고점에서 초당 요청 수 X 99 백분위 수 지연시간(단취:초)) + 오버헤드를 대비한 소량의 추가 스레드**



## 히스트릭스 세부설정

```java
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
```

확인할 통계 간격이 작고 유지하는 버킷 수가 많을수록 대량 서비스에서 CPU 및 메모리 사용량은 증가한ㄷ. 이 점을 인지하고 측정 지표와 수집 간격과 세분화된 버킷을 설정하고 싶은 유혹에 맞서야 한다.

히스트릭스 환경을 구성할 때는 히스트릭스의 세 가지 구성 레벨을 꼭 기억해야 한다. 

- 애플리케이션 기본 값 
- 클래스 기본값 
- 클래스 아넹서 정의된 스레드 풀 레벨



## 클래스 레벨에서 히스트릭스 전부 설정하기

> 운영 시스템에서 타임아웃이나 스레드 풀 개수처럼 조정을 많이 할 히스트릭스 데이터는 외부 스프링 클라우드 컨피그 서버에 저장한다. 이 방법을 사용하면 매개변수를 변경해야 할 때 애플리케이션을 재 컴파일이나 재배포할 필요 없이 값을 변경한 후 서비스 인스턴스를 재시작 하면된다.
>
> 그리고 @DefaultProperties를 이용하면 @RequestMapping 처럼 클래스 레벨에서 메소드들이 갖는 기본 프로퍼티들을 설정할 수 있다.
>
> ```java
> @DefaultProperties(
>  commandProperties = {
>  @HystrixProperty(
>      name="execution.isolation.thread.timeoutInMilliseconds",value="10000")})
> //메소드레벨에서 히스트릭스 설정 정의 
> //이 클래스는 타임아웃이 10초로 설정됐다.
> class SomeService {...}
> ```
>
> 히스트릭스 환경을 구성할 때는 히스트릭스의 세 가지 구성 레벨을 꼭 기억해야 한다.
>
> - 애플리케이션 기본값
> - 클래스 기본값
> - 클래스 안에서 정의된 스레드 풀 레벨

## @HystrixCommand 애너테이션의 구성 값

- fallbackMethod는 기본값이 없고, 원격 호출에서 타임아웃이 발생할 때 호출되는 클래스 내부의 메서드를 인식한다. 콜백 메서드는 @HystrixCommand 애너테이션이 붙은 클래스 안에 있어야 하며, 호출 클래스에서 사용된 동일한 메서드 서식이어야 한다. 설정된 값이 없으면 히스트릭스가 예외를 발생시킨다. 실패를 하면 대체 메소드를 정해주는 설정
- threadPoolKey 기본값이 없고, @HystrixCommand에 고유 이름을 부여하고 기본 스레드 풀과 독립된 스레드 풀을 생성한다. 설정된 값이 없으면 히스트릭스는 기본 스레드 풀을 사용함. 독자적인 threadPool을 갖는 것임.
- threadPoolProperties는 기본값이 없고, 스레드 풀의 동작을 구성하는 히스트릭스 애너테이션의 핵심 속성이다. 쓰레드 관련된 설정을 하는 것이다.
- coreSize는 기본값이 10이며, 스레드 풀 크기를 설정함.
- maxQueueSize는 기본값이 -1이며, 스레드 풀 앞 단에 설정할 큐의 최대 크기다. -1로 설정하면 큐가 사용되지 않으므로. 히스트릭스는 스레드가 처리 가용 상태가 될 때까지 대기한다. 더 자세한 내용은 예제를 보자
- circuitBreaker.requestVolumeThreshld 는 기본값이 20이며, 반복 시간 간격중 히스트릭스가 회로 차단기의 차단 여부를 검토하는데 필요한 최소 요청 수를 설정한다. 이 시간동안 어떤 조건에 맞게 오류가 발생하면 회로차단기가 시작됨. 참고로 이 값은 commandPoolProperties 속성에서만 설정된다.
- circuitBreaker.errorThresholdPercentage는 기본 값이 50이며, 반복 시간 간격 중 회로 차단기를 차단하는데 필요한 실패 비율이다. X초 동안 1/2이 실패하면 회로차단함.
- circuitBreaker.sleepWindowInMilliseconds는 기본 값이 5,000이며, 회로 차단기를 차단한 후 히스트릭스가 서비스 호출 시도를 대기하는 시간 이다. 회로 차단 한 다음 5초뒤에 다시 해당 메소드를 호출해서 회복이 되었는지 확인함. 이 값은 commandPoolProperties 속성에서만 설정된다.
- metricsRollingStats.timeInMilliseconds는 기본 값이 10,000이며 히스트릭스가 10초 동안 시간 간격에서 수집하고 모니터링할 통계 시간이다.
- metricsRollingStats.numBuckets은 기본 값이 10이며, 히스트릭스가 모니털이 시간 간격에서 유지할 측정 지표의 버킷 수다. 모니터링 시간 동안 더 많은 버킷을 사용하면 장애를 모니터링할 시간은 더 줄어든다.

참고로  metricsRollingStats.timeInMilliseconds/metricsRollingStats.numBuckets 의 몫이 통계 데이터의 길이다.
즉 나머지가 0으로 균등하게 분할해야 한다. 

ex)

```java
@HystrixCommand(//fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool", // threadPool의 이름을 설정
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize",value="30"), //스레드 풀의 개수를 설정
                     @HystrixProperty(name="maxQueueSize", value="10")}, // 스레드 풀 앞에 배치할 큐와 큐에 넣을 요청 수를 정의 / 값을 -1로 설정하면 동기식큐가 되며
            //스레드 풀의 개수만큼밖에 처리를 못하며 SynchronousQeue를 사용. 1보다 큰 값을 사용하면 LinkedBlockingQueue를 사용하며 많은 요청을 큐에 넣을 수 있다. 스레드 풀이 처음 초기화될 때만 설정할 수 있다.
            //스레드 풀의 적정 크기는(서비스가 정상일 때 최고점에서 초당 요청 수 X 99 백분위 수 지연시간(단위: 초)) + 오버헤드를 대비한 소량의 추가 스레드
            commandProperties={
                     @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"), //히스트릭스가 호출 차단을 고려하는 데 필요한 시간
                     @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"), // 회로 차단기를 차단하고 나서 requestVolumeThreshold 값 만큼 호출한 후 타임아웃이나 예외 발생, HTTP500 반환등으로 실패해야 하는 호출 비율
                     @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"), // 차단되고 나서 히스트릭스가 서비스 회복 상태를 확인할 때 까지 대기할 시간 간격
                     @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"), // 히스트릭스가 서비스 호출 문제를 모니터할 시간 간격을 설정하는데 사용 기본값은 1만 밀리초, 즉 10초
                     @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5")}// 설정한 시간 간격 동안 통계를 수집할 횟수를 설정 timeInMilliseconds와 나눴을 때 나머지가 0으로 균등하게 분할해야함. 15초 동안 5초길이로 하니까 3개의 통계 데이터가 나옴 .

    public List<License> getLicensesByOrg(String organizationId){
        logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();

        return licenseRepository.findByOrganizationId(organizationId);
    }
```

## 스레드 컨텍스트와 히스트릭스

@HystrixCommand가 실행될 때 THREAD(스레드)와 SEMAPHORE(세마포어, 역주) 신호장치)라는 두 가지 다른 격리 전략을 수행할 수 있다. 기본적으로 히스트릭스는 스레드 격리 전략을 수행한다. 호출을 보호하는 데 사용된 모든 히스트릭스 명령은 호출을 시도한 부모 스레드와 컨텍스트를 공유하지 않는 `격리된 스레드 풀` 에서 수행된다. 이는 히스트릭스가 자기 통제하에서 원래 호출을 시도한 부모 스레드와 연관된 어떤 활동도 방해하지 않고 스레드 실행을 중단할 수 있다는 것을 의미한다.

세마포어 방식은 이와 반대로 자신을 호출한 부모 스레드를 중단시킨다. 톰캣처럼 동기식 컨테이너 서버 환경에서 부모 스레드를 중단하면 개발자가 예외 처리를 할 수 없는 예외가 발생하여 예기하지 않은 결과가 발생할 수 있다.

명령 풀에 대한 격리 설정을 하려면 다음과 같이 작성한다.

```java
@HystrixCommand(
    commandProperties = {
        @HystrixProperty(
            name="execution.isolation.strategy",value="SEMAPHORE")})
```

> 기본적으로 히스트릭스 팀은 대부분의 명령에 대해 기본 격리 전략인 스레드 방식을 권장한다. 스레드 격리 방식은 히스트릭스 명령 스레드와 부모 스레드 사이에 격리 수준을 높이며, 세마포어 격리 방식보다 무겁다. 세마포어 격리 모델은 경량이며, 서비스에서 대용량을 처리하고 비동기 I/O 프로그래밍 모델(예를 들어 Netty 같은 비동기 I/O 컨테이너 사용)을 적용할 때 사용해야 한다.

## ThreadLocal과 히스트릭스

ThreadLocal : 스레드가 사용하는 전역변수

기본적으로 히스트릭스는 부모 스레드의 컨텍스트를 히스트릭스 명령이 관리하는 스레드에 전파하지 않는다. 예를 들 어 부모스레드에 ThreadLocal로 설정된 값은 기본적으로 부모 스레드가 호출하는 메서드에서 사용할 수 없고 @HystrixCommand 객채로 보호된다(다시 설명하면 THREAD 격리 수준을 사용하고 있다 가정한다.)

### HystrixConcurrencyStrategey 동작

히스트릭스는 스레드간의 격리로 스레드로컬이 전달 되지 않는데, 전달을 하고 싶으면 HystrixConcurrencyStrategey을 사용하면 된다.

히스트릭스를 사용하면 히스트릭스 호출을 감싸는 병행성 전략(concurrency strategy)을 사용자 정의하고 부모 스레드의 컨텍스트를 히스트릭스 명령이 관리하는 스레드에 `주입할` 수 있다. 사용자 정의된 HystrixConcurrencyStrategy를 구현하려면 다음 작업을 수행해야 한다. 

1. 히스트릭스 병행성 전략 클래스를 사용자 정의하기
2.  히스트릭스 명령에 userContext를 주입하도록 Callable 클래스 정의하기
3. 히스트릭스 병행성 전략을 사용자 정의하기 위해 스프링 클라우드 구성하기.

기본적으로 히스트릭스에서는 애플리케이션을 위한 하나의 HystrixConcurrencyStrategy만 정의할 수 있다.
스프링 클라우드는 이미 스프링 보안 정보를 전달하기 위한 병행성 전략을 정의하고 있다.
다행히 스프링 클라우드는 히스트릭스의 병행성 전략을(체인) 연결하는 기능을 제공한다.

> #### ThreadLocalAwareStrategy.java

```java
public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy{
    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    public ThreadLocalAwareStrategy(
            HystrixConcurrencyStrategy existingConcurrencyStrategy) { // 스프링 클라우드가 미리 정의한 병행성 클래스를 전달한다.
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) { 
        // existingConcurrencyStrategy 메서드 구현을 호출하거나 부모 HystrixConcurrencyStrategy 메서드를 호출한다.
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy
                //UserContext를 설정할 Callable 구현체를 주입한다 
                .wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()))
                : super.wrapCallable(new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext()));
    }
}
```

마지막 부분의 `wrapCallable()` 메서드는 Callable을 구현한 DelegatingUserContextCallable을 매개변수로 이 메서드에 전달한다. 이 매개변수는 사용자 REST 서비스 호출을 실행하는 부모 스레드의 UserContext를 작업이 수행되는 메서드를 보호하는 히스트릭스 명령 스레드에 설정하는 데 사용된다.

다음은 부모 스레드의 스레드 컨텍스트르를 히스트릭스 명령에 전파하는 다음 단계는 작업을 수행할 Callable 클래스를 구현하는 것이다 

> #### DelegatingUserContextCallable.java

```java
public final class DelegatingUserContextCallable<V> implements Callable<V> {
    private final Callable<V> delegate;
    private UserContext originalUserContext;
        //사용자 정의 Callable 클래스에 히스트릭스로 보호된 코드를 호출하는
        // 원본 Callable 클래스와 부모 스레드에서 받은 UserContext를 전달한다.
    public DelegatingUserContextCallable(Callable<V> delegate,
                                             UserContext userContext) {
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

      //@HysrixCommand 애너테이션이 메서드를 보호하기 전에 호출되는 Call() 함수다.
    public V call() throws Exception {
      //UserContext가 설정된다. 
      //UserContext를 저장하는 ThreadLocal 변수는 히스트릭스가 보호하는 메서드를 실행하는 스레드에 연결된다.
        UserContextHolder.setContext( originalUserContext );

        try {
          //UserContext가 설정되면 히스트릭스가 보호하는 메서드의 call() 메서드를 호출한다.
            return delegate.call();
        }
        finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate,
                                         UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}
```

히스트릭스로 보호된 메서드를 호출하면 히스트릭스와 스프링 클라우드는 DelegatingUserContextCallable 클래스 인스턴스를 만들고 보통 히스트릭스 명령 풀에서 관리하는 스레드에서 호출될 Callable 클래스를 전달한다. 이 코드 예에서 Callable 클래스는 delegate라는 자바 프로퍼티에 저장된다. 개념적으로 delegate 프로퍼티를 @HstrixCommand 애너테이션에서 보호되는 메서드에 대한 핸들로 생각할 수 있다.

위임된(delegated) Callable 클래스 외에도 스프링 클라우드는 호출을 시작한 부모 스레드에서 받은 UserContext 객체를 함께 전달한다. 인스턴스가 생성될 때 이 두값이 설정되면 실제 작업은 DelegatingUserContextCallable 같은 사용자 클래스의 call() 메서드에서 일어난다.

call() 메서드에서 수행해야 하는 첫 번째 작업은 UserContextHolder.setContext() 메서드를 사용해 UserContext를 설정하는 것이다. setContext() 메서드는 실행 중인 스레드별 ThreadLocal 변수에 UserContext 객체를 저장한다는 것을 기억하자. UserContext를 설정한 후 위임된 Callable 클래스의 call() 메서드를 호출한다. delegate.call()을 호출하면 @HystrixCommand 애너테이션이 보호하는 메서드가 호출된다.

### 히스트릭스 병행성 전략을 사용자 정의하기 위해 스프링 클라우드 구성

이제 ThreadLocalAwareStrategy 클래스를 사용한 HystrixConcurrencyStrategy와 DelegationUserContextCallable을 사용해 정의한 Callable 클래스가 준비되었으므로 스프링 클라우드와 히스트릭스에서 이 클래스들을 후킹하도록 연결해야 한다. 이를 이해 ThreadLocalConfiguration이라는 새로운 구성 클래스를 정의해야 한다.

```java
@Configuration
public class ThreadLocalConfiguration {
  @Autowired(required=false)
  //구성 객체가 생성될 때 기존 HystrixConcurrencyStrategy와 autowire 한다.
  private HystrixConcurrencyStrategy existingConcurrentcyStrategy; 

  @PostConstruct
  public void init() {

  }
}
```



추후에 Oauth 구현할 때 공부해야징.