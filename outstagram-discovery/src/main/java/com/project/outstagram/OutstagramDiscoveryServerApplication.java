package com.project.outstagram;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class OutstagramDiscoveryServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(OutstagramDiscoveryServerApplication.class, args);
    }

}
