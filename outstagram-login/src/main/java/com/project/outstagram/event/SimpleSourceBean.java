package com.project.outstagram.event;

import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {
    // 발행자, 메세지를 내보내는 것임
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    //스프링 클라우드 스트림은 서비스가 사용할 소스 인터페이스 구현을 주입한다.
    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishLoginChange(String action, String someId) {
        logger.debug("Sending Kafka message {} for ");
    }


}
