# Discovery Server

## TODO

- 디스커버리서버 클러스팅 하는 방법

## 디스커버리 서버의 특징 

- 고가용성(high available) : 서비스 디스커버리는 서비스 검색 정보를 서비스 디스커버리 클러스터의 여러 노드가 공유하는 핫클러스터링 환경을 지원해야 한다. 한 노드가 사용할 수 없게 되면 클러스터의 다른 노드가 인계를 받을 수 있어야 한다.
- 피어 투 피어(P2P Peer-to-Peer) : 서비스 디스커버리 클러스터의 각 노드는 서비스 인스턴스의상태를 공유 한다.
- 부하 분산(load-banlancing) : 서비스 디스커버리는 요청을 동적으로 부하 분산해서 서비스 디스커버리가 관리하는 모든 서비스 인스턴스에 분배해야 한다. 여러 면에서 초기 웹 애플리케이션을 구현하는 데 사용되었고, 더 정적이며 수동으로 관리되는 로드 밸런서는 서비스 디스커버리로 대체된다.
- 회복성(resilient) : 서비스 디스커버리 클라이언트는 서비스 정보를 로컬에 캐시 해야한다. 로컬 캐싱은 서비스 디스커버리 기능을 점진적으로 저하시킬 수 있는데 서비스 디스커버리 서비스가 가용하지 않을 때 애플리케이션 로컬 캐시에 저장된 정보를 기반으로 서비스를 계속 찾을 수 있고 동작하게 한다.
- 장애 내성(fault-tolerant) : 서비스 디스커버리는 서비스 인스턴스의 비정상을 탐지하고 가용 서비스 목록에서 인스턴스를 제거해야 한다. 그리고 이러한 서비스 장애를 감지하고 사람의 개입 없이 조치를 취해야 한다.

## 디스커버리 구현체

- 서비스 등록 : 서비스를 서비스 디스커버리 에이전트에 어떻게 등록하는가?
- 클라이언트가 서비스 주소 검색 : 서비스 클라이언트가 어덯게 서비스 정보를 검색하는가?
- 정보 공유 : 서비스 정보를 노드 간에 어떻게 공유하는가?
- 상태 모니터링 : 서비스가 자신의 상태 정보를 서비스 디스커버리 에이전트에 어떻게 전달하는가?



## 모든 인스턴스 보기

http://\<eureka service>:8761/eureka/apps/\<APPID>



```
http://localhost:8761/eureka/apps/outstagram-login
```



```xml
<application>
    <name>OUTSTAGRAM-LOGIN</name>
    <instance>
        <instanceId>172.30.1.13:outstagram-login:13333</instanceId>
        <hostName>172.30.1.13</hostName>
        <app>OUTSTAGRAM-LOGIN</app>
        <ipAddr>172.30.1.13</ipAddr>
        <status>UP</status>
        <overriddenstatus>UNKNOWN</overriddenstatus>
        <port enabled="true">13333</port>
        <securePort enabled="false">443</securePort>
        <countryId>1</countryId>
        <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo">
            <name>MyOwn</name>
        </dataCenterInfo>
        <leaseInfo>
            <renewalIntervalInSecs>1</renewalIntervalInSecs>
            <durationInSecs>2</durationInSecs>
            <registrationTimestamp>1575738178442</registrationTimestamp>
            <lastRenewalTimestamp>1575738261910</lastRenewalTimestamp>
            <evictionTimestamp>0</evictionTimestamp>
            <serviceUpTimestamp>1575738178442</serviceUpTimestamp>
        </leaseInfo>
        <metadata>
            <management.port>13333</management.port>
        </metadata>
        <homePageUrl>http://172.30.1.13:13333/</homePageUrl>
        <statusPageUrl>http://172.30.1.13:13333/actuator/info</statusPageUrl>
        <healthCheckUrl>http://172.30.1.13:13333/actuator/health</healthCheckUrl>
        <vipAddress>outstagram-login</vipAddress>
        <secureVipAddress>outstagram-login</secureVipAddress>
        <isCoordinatingDiscoveryServer>false</isCoordinatingDiscoveryServer>
        <lastUpdatedTimestamp>1575738178442</lastUpdatedTimestamp>
        <lastDirtyTimestamp>1575738178321</lastDirtyTimestamp>
        <actionType>ADDED</actionType>
    </instance>
</application>
```





## TIP

eureka 라이브러리 안에 ribbon이 포함되어있음.



## Client-Side-LoadBalancing

- DiscoveryClient : 코드량이 많아짐
- 리본을 지원하는 RestTemplate : DiscoveryClient의 더 나은 방법? 정도  개인적으로 선호
- Feign 클라이언트 : 인터페이스를 사용하며 동적 프록시 생성 방법





# Histrix

## 클라이언트 회복성 패턴이란 ?

클라이언트의 회복성을 위한 소프트웨어 패턴은 원격 서비스가 에러를 던지거나 제대로 동작하지 못해 원격 자원의 접근이 실패할 때, 원격자원(예를 들어 마이크로서비스 호출또는 데이터베이스 검색)을 호출하는 클라이언트 충돌을 막는 데 초점이 맞추어져 있다. 이 패턴의 목적은 데이터베이스 커넥션 및 스레드 풀 같은 소중한 클라이언트의 소비자에게 `상향` 전파 되는것을 막는다.

### 클라이언트 회복성 패턴

- 클라이언트 측 부하 분산 : 리본이나 로드밸런싱
- 회로 차단기(circuit. breaker) : 서비스 클라이언트가 장애 중인 서비스를 반복적으로 호출하지 못하게 한다.
- 폴백(fallback) : 호출이 실패하면 폴백은 실행 가능한 대안이 있는지 확인한다.
- 벌크헤드(bulkhead) : 불량 서비스가 클라이언트의 모든 자원을 고갈시키지 않도록 서비스 클라이언트가 수행하는 서비스 호출을 격리한다.

### 클라이언트 측 부하 분산

클라이언트가 넷플릭스 유레카 같은 서비스 디스커버리 에이전트를 이용해 서비스의 모든 인스턴스를 검색한 후 해당 서비스 인스턴스의 실제 위치를 캐싱하는 것이다.

서비스 소비자가 서비스 인스턴스를 호출해야 할 때마다 클라이언트 측 로드 밸런서는 서비스 위치 풀에서 관리하는 서비스 위치를 하나씩 전달한다.

클라이언트 측 로드 밸런서는 서비스 클라이언트와 서비스 소비자 사이에 위치하므로 서비스 인스턴스가 에러를 전달하거나 불량 동작하는지 감지한다. 클라이언트 측 로드 밸런서가 문제를 감지할 수 없다면 가용 서비스 위치 풀에서 문제가 된 서비스 인스턴스를 제거해 서비스 호출이 그 인스턴스로 전달되는 것을 막는다.

이 행위는 넷플릭스의 리본 라이브러리가 추가 구성 없이 기본적으로 하는 일과 동일하다.



### 회로 차단기

회로 차단기 패턴은 전기 회로의 차단기를 본떠 만든 클라이언트 회복성 패턴이다. 전기 시스템에서 회로 차단기는 전기선에 유입된 과전류를 감지한다. 회로 차단기가 문제를 감지하면 모든 전기 시스템과 연결된 접속을 차단하고 하부 컴포넌트가 과전류에 손상되지 않도록 보호하는 역할을 한다.

소프트웨어 회로 차단기는 원격 서비스 호출을 모니터링한다. 호출이 오래 걸린다면 회로 차단기가 중재해 호출을 중단한다. 회로 차단기는 원격 자원에 대한 모든 호출을 모니터링하고, 호출이 필요한 만큼 실패하면 회로 차단기가 활성화되어 빨리 실패하게 만들며, 고장 나 ㄴ원격 자원은 더 이상 호출되지 않도록 차단한다. 



### 폴백처리

원격 서비스에 대한 호출이 실패할 때 예외를 발생시키지 않고 서비스 소비자가 대체 코드 경로를 실행해 다른 방법으로 작업을 수행할 수 있다. 일반적으로 이 패턴은 다른 데이터 소스에서 데이터를 찾거나 향후 처리를 위해 사용자 요청을 큐에 입력하는 작업과 연관된다. 사용자 호출에 문제가 있다고 예외를 표시하지 않지만 나중에 해당 요청을 수행할 수 있다고 전달받을 수 있다.

사용자 행동 양식을 모니터링하고 구매 가능성 있는 다른 물품들을 추천하는 기능을 제공하는 전자상거래 웹사이트가 있다고 가정하자. 일반적으로 마이크로서비스를 호출해 사용자의 과거 행위를 분석하고 특정 사용자에게 맞춤화된 추천 목록을 전달한다. 하지만 선호도 서비스가 고장난다면 폴백은 모든 사용자의 구매 정보를 기반으로 더 일반화된 선호 목록을 조회한다. 그리고 이 데이터는 완전히 다른 서비스와 데이터 소스에서 추출된다. 



### 벌크헤드

벌크헤드 패턴은 선박을 건조하는 개념에서 유래한다. 벌크헤드 설계를 적용하면 배는 격벽이라는 완전히 격리된 수밀 구획으로 나뉜다. 설체에 구멍이 뚫린 경우에도 배가 수밀 구획(격벽)으로 분리되어 있으므로 침수 구역을 제한하고 배 전체의 침수와 침몰을 방지할 수 있다.

여러 원격 자원과 상호 작용해야 하는 서비스에도 이러한 벌크헤드 개념을 적용할 수 있다. 벌크헤드 패턴을 적용하면 원격 자원에 대한 호출을 자원별 스레드 풀로 분리하므로 특정 원격 자원의 호출이 느려져 전체 애플리케이션이 다운될 수 있는 위험을 줄일 수 있다. 스레드 풀은 서비스를 위한 벌크헤드(격벽) 역할을 한다. 각 원격 자원은 분리되어 스레드 풀에 할당된다. 한 서비스가 느리게 반응한다면 해당 서비스 호출을 위한 스레드 풀은 포화되어 요청을 처리하지 못하겠지만 다른 스레드 풀에 할당된 다른 서비스 호출을 포화되지 않는다.



## 히스트릭스

히스트릭스는 원격 자원 사이에 존재하며 클라이언트를 보호하는데, 데이터베이스를 호출하든 REST 기반 서비스를 호출하든 상관없다.

히스트릭스와 스프링 클라우드는 @HystrixCommand 어노테이션을 사용해 히스트릭스 회로 차단기가 관리하는 자바 클래스 메소드라고 표시한다. 스프링 프레임워크가 @HystrixCommand를 만나면 메소드를 감싸는 프록시를 동적으로 생성하고 원격 호출을 처기하기 위해 확보한 스레드가 있는 스레드 풀로 해당 메소드에 대한 모든 호출을 관리한다.





## trouble shooting

### 디스크버리 서버에서 로그 오류들이 막 나올때

```
019-12-14 16:58:01.012 ERROR 70437 --- [get_localhost-0] c.n.e.cluster.ReplicationTaskProcessor   : Batch update failure with HTTP status code 404; discarding 1 replication tasks
2019-12-14 16:58:01.012  WARN 70437 --- [get_localhost-0] c.n.eureka.util.batcher.TaskExecutors    : Discarding 1 tasks of TaskBatchingWorker-target_localhost-0 due to permanent error
2019-12-14 16:58:01.867 ERROR 70437 --- [get_localhost-0] c.n.e.cluster.ReplicationTaskProcessor   : Batch update failure with HTTP status code 404; discarding 1 replication tasks
2019-12-14 16:58:01.867  WARN 70437 --- [get_localhost-0] c.n.eureka.util.batcher.TaskExecutors    : Discarding 1 tasks of TaskBatchingWorker-target_localhost-0 due to permanent error
2019-12-14 16:58:02.873 ERROR 70437 --- [get_localhost-0] c.n.e.cluster.ReplicationTaskProcessor   : Batch update failure with HTTP status code 404; discarding 1 replication tasks
2019-12-14 16:58:02.873  WARN 70437 --- [get_localhost-0] c.n.eureka.util.batcher.TaskExecutors    : Discarding 1 tasks of TaskBatchingWorker-target_localhost-0 due to permanent error
2019-12-14 16:58:03.890 ERROR 70437 --- [get_localhost-3] c.n.e.cluster.ReplicationTaskProcessor   : Batch update failure with HTTP status code 404; discarding 1 replication tasks
2019-12-14 16:58:03.890  WARN 70437 --- [get_localhost-3] c.n.eureka.util.batcher.TaskExecutors    : Discarding 1 tasks of TaskBatchingWorker-target_localhost-3 due to permanent error
```

유레카서버에서 서비스들이 정상적으로 등록되지만 이런 오류들이 나올때는

```yaml
eureka:
  instance:
#    이거써줘야 오류가 안나네 https://github.com/spring-cloud/spring-cloud-netflix/issues/291
    hostname: localhost
  client:
    service-url:
      defaultZone: http://localhost:8761
#    유레카 서비스에 (자신을) 등록하지 않는다.
    register-with-eureka: false
#    레지스트리 정보를 로컬에 캐싱하지 않는다.
    fetch-registry: false
  server:
    # 서버가 요청을 받기 전 대기할 초기 시간
    # 운영 서버일때는 주석처리해야한다. 그래야 바로 서비스로 인식한다.
#    wait-time-in-ms-when-sync-empty: 5

```



hostname을 설정해줘야 한다. 맥이나 리눅스에서 해줘야한다고 한다.

[https://github.com/spring-cloud/spring-cloud-netflix/issues/291](https://github.com/spring-cloud/spring-cloud-netflix/issues/291)