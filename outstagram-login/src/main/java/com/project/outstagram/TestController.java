package com.project.outstagram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Value("${spring.datasource.url}")
    private String url;

    @GetMapping("/test")
    public String test() {
        return url;

    }
}
