# Zipkin





## 트러블 슈팅

Zipkin이 전송되는걸 하려면 logging을 root 레벨로 주면 된다.



```yaml
#이거 밑에꺼 쓰면 집킨한테 전송이 안된다.. 왜 안되지? 아오..
#spring:
#  zipkin:
#    baseUrl: localhost:9411
```



스프링 예제에서는 zipkin url을 다음과 같이 주라고 나와있는데, 이걸 사용하면 오류가 나서 zipkin으로 메세지가 안날라간다. 주의!~ 