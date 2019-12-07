# Discovery Server

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

