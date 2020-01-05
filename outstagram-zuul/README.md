# Zuul

# 스프링 클라우드의 주울(Zuul)로 서비스 라우팅하기

Zuul을 사용하는 이유는 여러 서비스 호출사이에 발생하는 보안과 로깅, 사용자 추적 등 주요 행위를 확인해야 할 시점이 온다.
이 기능을 구현할 때 각 개발 팀은 자체 솔루션을 구축하지 않고 이러한 속성이 모든 서비스에 일관되게 적되게 하고 싶을 것이다. 공통 라이브러리나 프레임워크를 사용해 서비스마다 이러한 기능을 직접 구축할 수 있지만 다음 세 가지를 염두에 두어야 한다.

1. 구축 중인 각 서비스에 이러한 기능을 일관되게 구현하기 어렵다.
2. ㅇㅣ러한 기능을 적절하게 구현하기 어렵다. 마이크로서비스에 대한 보안 기능들을 구현 중인 각 서비스에 설정하고 구성하기 어려울 수 있다. 
3. 서비스 간 복잡한 의존성을 만든다. 전체 서비스가 공유하는 공통 프레임워크에 더 많은 기능을 추가할수록 서비스의 재컴파일과 재배포 없이 공통 코드의 동작 변경이나 추가를 하는 일이 더 어려워 진다.

이 문제를 해결하려면 특정 서비스에서 이러한 횡단 관심사들을 `추상화` 하고 독립적인 위치에서 애플리케이션의 모든 마이크로서비스 호출에 대한 필터와 라우터 역할을 해야 한다. 이러한 횡단 관심사를 서비스 게이트웨이(services gateway)라고 한다. 서비스 클라이언트가 서비스를 직접 호출하지 않고 `단일한 정책 시행`지점 역할을 하는 서비스 게이트웨이로 모든 호출을 경유시켜 최종 목적지로 라우팅 한다.

### 순서

- 하나의 URL 뒤에 모든 서비스를 배치하고 서비스 디스커버리를 이용해 모든 호출 실제 서비스 인스턴스로 매핑한다.

  호출순서는 서비스게이트웨이호출 하면 뒷단데 디스커버리들이 쫙 깔린 형태이다.

- 서비스 게이트웨이를 경유하는 모든 서비스 호출에 상관관계 ID를 삽입한다. 

- 호출할 때 생성된 상관관계 ID를 HTTP 응답에 삽입하고 클라이언트에 회신한다. 

- 대중이 사용 중인 것과 다른 조직 서비스 인스턴스 엔드포인트로 라우팅하는 동적 라우팅 메커니즘을 구축한다.

서비스 게이트웨이 클라이언트가 각 서비스에 보내는 모든 호출 사이에 위치하므로 게이트웨이는 서비스 호출에 대한 `중앙 집중식 정책 시행 지점`역할도 한다. 중앙 집중식 PEP를 사용한다는 것은 각 개발 팀이 이러한 관심사를 구현하지 않고도 서비스의 횡단 관심사를 단일 지점에서 구현할 수 있다는 것을 의미한다. 서비스 게이트웨이에서 구현할 수 있는 횡단 관심사 예는 다음과 같다.

- 정적 라우팅(static routing) : 서비스 게이트웨이는 단일 서비스 URL과 API 경로로 모든 서비스를 호출하게 한다. 개발자는 모든 서비스에 대해 하나의 서비스 엔드포인트만 알면 되므로 개발이 간단해진다.
- 동적 라우팅(dynamic routing) : 서비스 게이트웨이는 유입되는 서비스 요청을 조사하고 요청 데이터를 기반으로 서비스 호출자 대상에 따라 지능형 라우팅을 수행할 수 있다. 예를 들어 베타 프로그램에 참여하는 고객의 서비스 호출은 모든 다른 코드 버전이 수행되는 특정 서비스 클러스터로 라우팅될 수 있다.
- 인증(authentication)과 인가(authorization) : 모든 서비스 호출은 서비스 게이트웨이로 라우팅되므로 서비스 게이트웨이는 서비스 호출자가 자신을 인증하고 서비스를 호출할 권한 여부를 확인할 수 있는 최적의 장소이다.
- 측정 지표 수집(metric collection)과 로깅(logging) : 서비스 게이트웨이를 사용하면 서비스 호출이 서비스 게이트웨이를 통과할 때 측정 지표와 로그 정보를 수집할 수 있다. 규격화된 로깅을 보장하기 위해 사용자 요청에서 주요 정보가 누락되지 않았는지 확인 하는 데도 사용된다. 이는 각 서비스에서 측정 지표를 수집할 필요가 없다는 것이 아니라 서비스 게이트웨이를 사용하면 서비스 호출된 횟수와 응답 시간처럼 많은 기본 측정 지표를 한곳에서 수집할 수 있다는 의미다.

> #### 서비스게이트웨이를 구현할 때 조심해야 할점!
>
> 로드 밸런서는 각 서비스 그룹(서비스게이트웨이가 로드밸런스를 가지란 소리인가???) 앞에 있을 때 유용하다. 이때 여러 서비스 게이트웨이 인스턴스 앞에 로드 밸런서를 두는 것은 적절한 설계이며, 서비스 게이트웨이를 확장할 수 있다. 모든 서비스 인스턴스 앞에 로드 밸런서를 두는 것은 병목점이 될 수 있어서 좋은생각은 아니다.
>
> 작성하는 서비스 게이트웨이 코드를 무상태로 유지하자. 서비스 게이트웨이의 정보를 메모리에 저장하지 말자. 주의하지 않으면 게이트웨이의 확장성을 제한하고 모든 서비스 게이트웨이 인스턴스에 데이터가 복제되도록 해야 한다.
>
> 작성하는 서비스 게이트웨이 코드를 가볍게 유지하자. 서비스 게이트웨이는 서비스 호출에 대한'병목점'이다. 여러 데이터베이스 호출이 포함된 복잡한 코드는 서비스 게이트웨이에서 추적하기 힘든 성능 문제의 원인이 된다.

### Zuul의 다양한 기능

- 애플리케이션의 모든 서비스 경로를 단일 URL로 매핑 : 주울의 매핑이 단일 URL로만 제한되는 것은 아니다. 주울에서는 여러 경로 항목을 정의해 경로 매핑을 매우 세분화할 수 있다.(각 서비스 엔드포인트는 고유한 경로로 매핑된다) 하지만 주울의 가장 일반적인 사용 사례는 모든 서비스 호출이 통과하는 단일 진입점을 구축하는 것이다.
- 게이트웨이로 유입되는 요청을 검사하고 대응할 수 있는 필터 작성 : 이러한 필터를 사용하면 코드에 정책 시행지점(PEP)을 주입해서 모든 서비스 호출에서 광범위한 작업이 일관된 방식으로 수행할 수 있다.



세가지 작업

1. 주울 스프링 부트 프로젝트를 설정하고 의존성 적절하게 구성
2. 스프링 부트 프로젝트를 중루 서비스로 설정하도록 스프링 클라우드 어노테이션으로 수정하기
3. 주울을 유레카와 통신하도록 구성하기



## Zuul Server 구성

```java
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
}
```

주울 프록시 서버는 기본적으로 스프링 제품과 동작하도록 설계되었다. 따라서 주울은 자동으로 유레카를 사용해 서비스 ID로 서비스를 찾은 후 넷플릭스 리본으로 주울 내부에서 요청에 대한 클라이언트 측 부하 분산을 수행한다.

#### dependecy

```groovy
compile group: 'org.springframework.cloud', name: 'spring-cloud-starter-netflix-zuul', version: '2.0.4.RELEASE'
```

```java
@SpringBootApplication
@EnableZuulProxy // 서비스를 주울 서버로 사용한다.
@EnableDiscoveryClient // 유레카 서버에 등록한다. 
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //멀티프로젝트 환경때문에 JPA 설정은 제외하라는 어노테이션
public class WorkerZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkerZuulApplication.class, args);
    }
}
```

> #### Note
>
> @EnableZuulServer 어노테이션을 사용하면 Zuul 리버스 프록시 필터를 로드하지 않은 주울 서버나 넷플릭스 유레카를 사용하는 주울 서버를 생성할 수 있다. @EnableZuulServer는 자체 라우팅 서비스를 만들고 내장된 주울 기능도 사용하지 않을 때 선택할 수 있다. 유레카가 아닌 서비스 디스커버리 엔진[예: 콘설(Consul)]과 통합할 경우 해당된다.

### 유레카 서버에 등록

```yaml
eureka:
  instance:
    prefer-ip-address: true #서비스간 통신 시 hostname 보다 ip 를 우선 사용 함
  client:
    register-with-eureka: true #자기 자신은 서비스로 등록하지 않는다.
    fetch-registry: true #마이크로서비스인스턴스 목록을 로컬에 캐시할 것인지
    serviceUrl:
      defaultZone: http://127.0.0.1:8761/eureka/
```

## 주울에서의 경로 구성

주울은 본래 리버스 프록시다. 리버스 프록시란 자원에 접근하려는 클라이언트와 자원 사이에 위치한 중개서버이다.
클라이언트는 프록시가 아닌(주울 서버가 아닌) 다른 서버와 통신하는 것 조차 알 수 없고, 리버스 프록시는 클라이언트의 요청을 받은 후 클라이언트를 대신해 원격 자원을 호출한다.

마이크로서비스 아키텍처에서 주울(리버스 프록시)은 클라이언트에서 받은 마이크로서비스 호출을 하위 서비스에 전달한다. 서비스 클라이언트는 주울과 통신한다고 생각한다.(원래는 주울이 마이크로서비스를 호출해서 return 해주는 거지만) 주울이 하위 클라이언트와 통신하려면 유입되는 호출을 어떻게 하위 경로로 매핑할지 알아야 한다. 그러기 위해 다음 메커니즘을 제공한다.

- 서비스 디스커버리를 이용한 자동 경로 매핑
- 서비스 디스커버리를 이용한 수동 경로 매핑
- 정적 URL을 이용한 수동 경로 매핑





여기에 경로구성에 대한 얘기가 나와야함. 왜 난 경로 구성이 안돼징? 



## 주울과 서비스 타임아웃

주울은 넷플릭스의 히스트릭스와 리본 라이브러리를 사용해 오래 수행되는 서비스 호출이 서비스 게이트웨이의 성능에 영향을 미치지 않도록 한다. 기본적으로 주울은 요청을 처리하는데 1초 이상 걸리는 모든 호출을 종료하고 HTTP 500 에러를 반환한다(이것은 히스트릭스 기본 동작이다.) 다행히도 주울 서버 구성에서 히스트릭스 타임아웃 프로퍼티를 설정해 이러한 동작을 구헝할 수 있다.

주울로 실행 중인 모든 서비스에 대해 히스트릭스 타임아웃을 설정하려면, hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds 프로퍼티를 사용할 수 있다. 

예를 들어 히스트릭스의 기본 타임아웃 시간을 2.5초로 설정하려면 주울의 스프링 클라우드 구성 파일에서 다음과 같이 구성할 수 있다.

```yaml
zuul.prefix : /api
zuul.routes.[service이름] : /[매팡할경로]/**
zuul.debug.request : true
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds : 2500
```

특정 서비스에 대해 별도의 히스트릭스 타임아웃을 설정하려면 프로퍼티의 default 부분을 재정의하려는 서비스의 유레카 서비스 ID 이름으로 바꿀 수 있다. 예를 들어 licensingservice의 타임아웃만 3초로 변경하고 나머지 서비스는 히스트릭스 기본 타임아웃 시간으로 설정하려면 구성 파일에서 다음과 같이 사용할 수 있다.

```yaml
hystrix.command.[서비스이름].execution.isolation.thread.timeoutInMilliseconds : 12500
```

위에는 히스트릭스 타임아웃을 재정의했지만 넷플릭스의 리본 역시 5초 이상 수행되는 호출을 타임아웃한다.
5초 이상 수행되는 호출 설계는 재검토가 강력히 권장되긴 하다.

```yaml
 hystrix.command.[A서비스].execution.isolation.thread.timeoutInMilliseconds : 2500
 [A서비스].ribbon.ReadTimeout : 7000
```



## 주울의 진정한 힘! 필터

주울 게이트웨이로 유입되는 모든 요청을 프록시해서 서비스 호출을 단순화 할 수 있지만, 주울의 진정한 능력은 게이트웨이를 통과하는 모든 서비스 호출에 대해 사용자 정의 로직을(서비스에대한 보안과 로깅 및 추적)을 작성할 때 드러난다.

이러한 정책을 구현하기 위해 각 서비스들을 수정하지 않고, 모든 서비스에 적용하기 원하기 때문에 이 정책들을 횡단 관심사로 간주한다.

마찬가지로 주울 필터는 서블릿 필터나 스프링 애스펙트와 유사한 방식으로 사용되어 다양한 동작을 가로채고 기본 코드를 작성한 작성자가 모르게 호출의 행동 양식을 꾸미거나 변경할 수 있다. 서블릿 필터나 스프링 애스펙트는 특정 서비스에 국한되지만 주울과 주울 필터를 사용하면 주울로 라우팅되는 모든 서비스에 대해 횡단 관심사를 구현할 수 있다.

주울은 다음 세 가지 필터 타입을 지원한다.

- 사전 필터(pre-filter) : 주울에서 목표 대상에 대한 실제 요청이 발생하기 전에 호출된다. 일반적으로 사전 필터는 서비스의 일관된 메세지 형식(예를 들어 주요 HTTP 헤더의 포함 여부)을 확인하는 작업을 수행하거나 서비스를 이용하는 사용자가 인증(본인 증명) 및 인가 (수행 권한 부여) 되었는지 확인하는 게이트키퍼 역할을 한다.
- 사후 필터(post filter) : 대상 서비스를 호출하고 응답을 클라이언트로 전송한 후 호출된다. 일반적으로 사후 필터는 대상 서비스의 응답을 로깅하거나 에러 처리, 민감한 정보에 대한 응답을 감시하는 목적으로 구현된다.
- 경로 필터(route filter) : 대상 서비스가 호출되기 전에 호출을 가로채는 데 사용된다. 일반적으로 경로 필터는 일정 수준의 동적 라우팅 필요 여부를 결정하는 데 사용된다. 예를 들어 동일 서비스의 다룬 두 버전을 라우팅할 수 있는 경로 단위 필터를 사용해 작은 호출 비율만 새 버전의 서비스로 라우팅 할 수 있다. 이렇게 하면 모든 사용자가 새로운 서비스를 이용하지 않고도 소수 사용자에게 새로운 기능을 노출할 수 있다.

## 필터의 호출 흐름 

1. 요청이 주울 게이트웨이에 유입되면 정해진 사전 필터가 호출된다. 사전 필터는 HTTP 요청이 실제 서비스에 돌다하기 전에 요청을 검사하고 수정한다. 사전 필터는 사용자를 다른 엔드포인트나 서비스로 향하게 할 수 없다.
2. 주울은 유입된 요청에 대해 사전 필터를 실행한 후 정해진 경로 필터를 실행한다. 경로 필터는 서비스가 향하는 목적지를 변경할 수 있따.
3. 경로 필터는 주울 서버가 전송하도록 구성된 경로가 아닌 다른 경로로 서비스 호출을 리다이렉션하는 것도 가능하다. 하지만 주울의 경로 필터는 HTTP 리다이렉션 대신 유입된 HTTP 요청을 종료한 후 원래 호출자를 대신해 그 경로로 호출한다. 이것은 경로 필터가 동적 경로 호출을 와전히 소유해야 하므로 HTTP 리다이렉션할 수 없다는 것을 의미한다.
4. 경로 필터가 호출자를 새로운 경로로 동적 리다이렉션하지 않는다면 주울 서버는 원래 대상 서비스의 경로로 보낸다.
5. 대상 서비스가 호출되었다면 주울의 사후 필터가 호출된다. 사후 필터는 호출된 서비스의 응답을 검사하고 수정할 수 있다.



```java
@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "tmx-auth-token";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORG_ID         = "tmx-org-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public String getCorrelationId(){
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(CORRELATION_ID) !=null) {
            return ctx.getRequest().getHeader(CORRELATION_ID);
        }
        else{
            return  ctx.getZuulRequestHeaders().get(CORRELATION_ID);
        }
    }

    public void setCorrelationId(String correlationId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(CORRELATION_ID, correlationId);
    }

    public  final String getOrgId(){
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(ORG_ID) !=null) {
            return ctx.getRequest().getHeader(ORG_ID);
        }
        else{
            return  ctx.getZuulRequestHeaders().get(ORG_ID);
        }
    }

    public void setOrgId(String orgId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(ORG_ID,  orgId);
    }

    public final String getUserId(){
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(USER_ID) !=null) {
            return ctx.getRequest().getHeader(USER_ID);
        }
        else{
            return  ctx.getZuulRequestHeaders().get(USER_ID);
        }
    }

    public void setUserId(String userId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(USER_ID,  userId);
    }

    public final String getAuthToken(){
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN);
    }

    public String getServiceId(){
        RequestContext ctx = RequestContext.getCurrentContext();

        //We might not have a service id if we are using a static, non-eureka route.
        if (ctx.get("serviceId")==null) return "";
        return ctx.get("serviceId").toString();
    }


}
```



일반적으로 스프링 MVC나 스프링 부트 서비스에서 RequestContext는 org.springframework.web.servletsupport.RequestContext 타입인데, 주울에서는 주울 정보에 액세스 하기위해 일부 메소드가 추가된 특별한 RequestContext를 제공한다. 



## 서비스 호출에서 상관관계 ID 사용

이제 다음과 같은 작업이 가능하다. 

- 호출이되는 마이크로서비스에서 상관관계 ID를 손쉽게 접근한다.
- 마이크로서비스가 호출하는 하위 서비스 호출에도 상관관계 ID를 전파한다.

이를 구현하기 위해 마이크로서비스에 3개의 클래스를 작성한다. 유입되는 HTTP 요청에서 상관관계 ID를 읽어 와서 잡근할 수 있는 클래스에 매핑한 후 하위클래스에게 전파하는데 사용된다.



1. 주울 게이트웨이로 서비스A가 호출되면 TrackingFilter는 주울로 유입되는 모든 호출의 HTTP 헤더에 상관관계 ID를 삽입한다.
2. UserContextFilter 클래스는 사용자 정의 HTTP ServletFilter이며 상관관계 ID를 UserContext에 매핑하는데, 나중에 호출할 때 사용할 수 있도록 스레드 로컬에 저장된 값임.
3. 서비스 B , C ,D에 대한 호출을 실행한다.
4. RestTemplate은 서비스를 호출하는데 사용되고, 사용자 정의된 Spring Interceptor 클래스(UserContextInterceptor)로 상관관계 ID를 아웃바운드 호출의 HTTp 헤더에 삽입한다 



## 유입되는 HTTP 요청을 가로채는 UserContextFilter

처음으로 만들어야 되는 클래스는 서비스에 유입되는 모든 HTTP 요청을 가로채서 HTTP 요청에서 상관관계 ID를 UserContext 클래스에 매핑하는 HTTP 서블릿 필터이다. 

```java
@Component
/**
 * 필턴느 스프링 @Component 어노테이션을 사용하고 javax.servlet.Filter 인터페이스를
 * 구현해 스프링에 등록되고 선택된다.
 */
public class UserContextFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        //상관관계 ID를 검색해 UserContext 클래스에 설정
        UserContextHolder.getContext().setCorrelationId(  httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

        logger.debug("Special Routes Service Incoming Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
```



## 서비스가 쉽게 액세스할 수 있는 HTTP 헤더를 만드는 UserContext

```java
/**
 * UserContext클래스는 마이크로서비스가
 * 처리하는 개별 서비스 클라이언트 요청에 대한 HTTP 헤더 값을 저장하는데 사용 된다.
 * 이 클래스는 정보를 저장하는 POJO 인데,
 * UserContextHolder.class가 스레드 로컬을 이용해 모든 스레드가 접근이 가능하게 만들어준다.
 */
@Component
public class UserContext {
    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "tmx-auth-token";
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
```



## ThreadLocal에 UserContext를 저장하는 UserContextHolder

```java
/**
 * ThreadLocal 변수에 UserContext를 저장한다. ThreadLocal 변수는
 * 요청을 처리하는 해당 스레드에서 호출되는 모든 메소드에서
 * 액세스 가능한 변수임.
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    public static final UserContext getContext(){
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public static final void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    public static final UserContext createEmptyContext(){
        return new UserContext();
    }
}
```



## 상관관계 ID의 전파를 보장하는 사용자 정의 RestTemplate과 UserContextInterceptor

```java 
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

```

### 빈 추가 

```java
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
```



> 로그 수집과 인증 등
>
> 이제 상관관계 ID가 각 서비스에 전달되므로 호출과 연관된 모든 서비스를 관통하는 트랜잭션을 추적할 수 있다. 이를 위해서는 각 서비스 로그를 중앙 집중식 로그 지점으로 보내 모든 서비스의 로그 항목을 단일 지점으로 캡쳐해야 한다. 로그 수집 서비스에서 캡처된 로그 항목에는 각각 상관관계 ID가 있다. 



## 상관관계 ID를 전달받는 사후 필터 작성

주울은 서비스 클라이언트를 대신 해 실제 HTTP 호출을 실행한다는 것을 기억하자. 주울은 대상 서비스 호출에 대한 응답을 검사한 후 수정하거나 추가 정보를 삽입할 수 있다. 주울 사후 필터는 사전 필터로 데이터를 캡처하는 것과 연계할 때 측정 지표를 수집하고 사용자 트랜잭션과 연관된 모든 로깅을 완료할 최적의 장소다. 마이크로 서비스 사이에 전달된 상관관계 ID를 사용자에게 다시 전달해 이러한 이점을 얻을 수 있다.



사후 필터를 사용해 서비스 호출자에게 다시 전달될 HTTP 응답 헤더에 상관관계 ID를 삽입해줄 수 있다.

```java
@Component
public class ResponseFilter extends ZuulFilter{
    private static final int  FILTER_ORDER=1;
    private static final boolean  SHOULD_FILTER=true;
    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Override
    public String filterType() {
        //사후 필터를 만들려면 필터 타입을 POST_FILTER_TYPE으로 설정한다.
        return FilterUtils.POST_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        logger.debug("Adding the correlation id to the outbound headers. {}", filterUtils.getCorrelationId());
        //원래 HTTP 요청에서 전달된 상관관계 ID를 가져와 응답에 삽입한다.
        ctx.getResponse().addHeader(FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId());

        //처음부터 끝까지 주울에 들어오고 나가는 요청 항목을 보여주기 위해 나가는 요청 URI를 기록하낟.
        logger.debug("Completing outgoing request for {}.", ctx.getRequest().getRequestURI());

        return null;
    }
}
```

서비스 호출을하면 응답으로 header에 txd 헤더가 찍힘



## 동적 경로 필터 작성

사용자 정의 경로 필터가 없다면, 앞부분에서 본것 처럼 매핑 정의를 기반으로 모든 라우팅을 수행한다.
하지만 주울 경로 필터를 작성하면 서비스 클라이언트의 호출을 라우팅하는 방식에 지능을 더 할 수 있다.



A/B 테스트 하는 얘기 일정 비율만 신기능을 쓰게끔 유도할 수 있음.

(추후에 공부해야겠다. 좀 복잡한듯)



## OAuth2 토근 전파

기본적으로 주울은 Cookie와 Set-Cookie, Authorization 같은 민감한 HTTP 헤더를 하위 서비스에 전달하지 않는다. 주울에서 'Authorization' HTTP 헤더를 전파하게 하려면 주울 서비스 게이트웨이의 application.yml 파일이나 스프링 클라우드. 컨피그 데이터 저장소에서 다음 구성 정볼르 설정해야 한다. 

```yaml
zuul.sensitiveHeaders : Cookie,Set-Cookie
```

이 구성 정보는 주울이 하위 서비스에 전파하지 않는 민감한 헤더의 차단 목록이다. 이 목록에 Authorization 값이 없다면 주울이 전파를 허용한다는 의미다. zuul.sensitiveHeaders를 아예 설정하지 않는다면 주울은 자동으로 세 가지 값(Cookie와 Set-Cookie,Authorization) 모두 전파하지 않는다.

> 주울의 다른 OAuth2 기능은?
>
> 주울은 @EnableAuth2SSO 어노테이션을 사용해 자동으로 OAuth2 액세스 토큰을 하위로 전파하고 OAuth2 서비스 요청을 인가할 수 있다. 



두번째로는 리소스를 보호하는 `ResourceServerConfigurerAdapter` 구현체를 작성하는 것

세번째로는 HTTP Authorization 헤더가 마이크로서비스 호출에 주입됐는지 확인해야 한다.
스프링 시큐리티가 없다면 HTTP 헤더를 가져오는 서블릿 필터를 작성하고 마이크로서비스들에서 나가는 모든 호출에 이 헤더를 직접 추가해야 한다. 스프링 OAuth2는 OAuth2 호출을 지원하는 새로운 RestTemplate 클래스인 OAuth2RestTemplate을 제공한다. OAuth2RestTemplate 클래스를 사용하려면 먼저 다른 OAuth2 보호 서비스를 호출하는 서비스에 자동 연결 될 수 있는 빈으로 노출 해야한다. 

### 보호되는 마이크로서비스 자원에 밑에 코드 넣기

```java
@Bean
public OAuth2RestTemplate restTemplate(UserInfoRestTemplateFactory factory) {
    return factory.getUserInfoRestTemplate();
}
```



```java
@Component
public class OrganizationRestTemplateClient {
    @Autowired
    OAuth2RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    public Organization getOrganization(String organizationId){
        logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://zuulserver:5555/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
```



## trouble shooting

### /actuator/routes

이 앤드포인트를 호출하면 서비스 경로들이 보이는데, 스프링 부트 2.0 부터는 숨겨져있어서 enable 해줘야한다.

application.yml에서

```
# /actuator/routes 엔드포인트를 호출하면 서비스 경로들이 보이는데
# 스프링 2.0 부터는 변경됨 .
management:
  endpoints:
    web:
      exposure:
        include:
          - "*"

#위와 같이 엔드포인트를 노출시켜줘야 한다.
```

[깃헙자료](https://github.com/spring-cloud/spring-cloud-netflix/issues/2813)

[공식문서](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide#endpoints)









밑에는 Zuul에 관련된 내용이 아님.

# 스프링 클라우드 슬루스와 집킨을 이용한 분산추적

마이크로서비스 아키텍처는 복잡한 모놀리식 소프트워어 시스템을 더 작고 다루기 쉬운 부분으로 분해 하고 독립적으로 빌드 및 배 포 할 수 있기 때문에 문제가 발생한 곳에서 디버깅하려는 것은 힘들다. 서비스가 분산 되어있다면 여러 서비스와 물리 머신, 다양한 데이터 저장소 사이에서 하나 이상의 트랜잭션을 추적하고 정확한 상황을 종합하려고 노력해야 한다.

## 방법 

- 상관관계 ID를 사용해 여러 서비스 사이의 트랜잭션을 서로 연결한다.
- 여러 서비스 사이의 로그 데이터를 검색 가능한 단일 소스로 수집한다. 
- 여러 서비스 사이의 사용자 트랜잭션 흐름을 시각화하고 트랜잭션 각 부분의 성능 특성을 이해한다.



## 기술 스택 

- 스프링 클라우드 슬루스 : 상관관계 ID를 사용해 HTTp 호출을 측정하는 스프링 클라우드 프로젝트이며, 생성 중인 추적 데이터를 오픈집킨에 공급할 수 있는 연결고리를 제공. 생성되는 상관관계 ID를 모든 시스템 호출로 통과시키기 위해 필터를 추가하고 다른 스프링 컴포넌트와 상호 작용하는 방식으로 슬루스를 활용.
- 페이퍼트레일 : 여러 데이터 소스의 로그 데이터를 검색이 가능한 단일 데이터베이스로 수집하는 클라우드 기반의 프리미움(freemium) 서비스.  로그 수집에 대해 사내 구축형(온프레스미)과 클라우드 기반,오픈 소스, 상용 솔루션 등 여러 대안은 있음.
- 집킨 : 여러 서비스 사이의 트랜잭션 흐름을 보여주는 오픈소스 기반의 데이터 시각화 도구. 집킨은 트랜잭션을 컴포넌트별로 분해하고 성능 과열점이 어디서 발생햇는지 시각적으로 확인이 가능하다.



## 슬루스의 역할

- 상관관계 ID가 없다면 상관관계 ID를 투명하게 생성하고 서비스 호출에 주입한다.
- 서비스에서 나가는 호출에 대한 상관관계 ID 전파를 관리해 트랜잭션의 상관관계 ID가 자동으로 나가는 호출에 추가된다.
- 상관관계 정보를 스프링 MDC 로그에 추가해 생성된 상관관계 ID가 스프링 부트의 기본 SL4J와 로그백 구현으로 자동적으로 로깅된다.
- 선택적으로 서비스의 추적 정보를 집킨 분삭 추적 플랫폼에 전송한다.

> 스프링 MDC
>
> 스프링 MDC는 즉 매핑 진단컨텍스트(MDC. Mapped Diagnostic Context)이다.





대규모 마이크로서비스 환경(특히 클라우드)에서 로그 데이터는 플랫폼을 디버깅하는 중요한 도구다. 하지만 마이크로서비스 기반의 애플리케이션 기능은 세분화된 서비스로 분산되어 있고, 한 종류의 서비스에 많은 서비스 인스턴스가 존재할 수 있기 때문에 사용자 문제를 해결하기 위해 여러 서비스에서 발생하는 로그 데이터를 한곳으로 연결하는 것은 상당한 어려운 일이다.



## 페이퍼트레일

- 무료형 계정으로 등록할 수 있는 프리미움 모델을 제공
- 설정이 매우 쉬움. 특히 도커 같은 컨테이너 런타임과 설정하기 쉽다.
- 클라우드 기반임.