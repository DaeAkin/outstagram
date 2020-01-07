package outstagram;


import brave.sampler.Sampler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@SpringBootApplication
@RestController
@Slf4j
public class OutstagramTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutstagramTestApplication.class, args);
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
