#도커 런타임에서 사용될 리눅스 도커 이미지이다. 이 이미지를 설치하면 자바 애플리케이션에 최적할 수 있음
FROM openjdk:8-jdk-alpine
#핑(ping) 유틸리티인 nc(netcat)를 설치해 의존하는 서비스 활성화 여부를 확인
RUN  apk update && apk upgrade && apk add netcat-openbsd

RUN mkdir -p /usr/local/outstagram-config-server
ADD outstagram-config-server-0.0.1-SNAPSHOT.jar /usr/local/outstagram-config-server/
ADD run.sh run.sh
RUN chmod +x run.shld
CMD ./run.sh