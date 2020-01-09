package outstagram;


import brave.sampler.Sampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@SpringBootApplication
@RestController
@Slf4j
//@EnableBinding(Sink.class)
public class OutstagramTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutstagramTestApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void eventReciveMethod(LoginChangeModel loginChangeModel) {

        log.debug("event is {}" , loginChangeModel.toString());
    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();

        return template;
    }

    @GetMapping("/test")
    public String hi() {
        log.info("hi");
        return "test";
    }

    @Bean
    public Sampler defaultSampler() {
        // return new AlwaysSampler();
        return Sampler.ALWAYS_SAMPLE;
    }

}
