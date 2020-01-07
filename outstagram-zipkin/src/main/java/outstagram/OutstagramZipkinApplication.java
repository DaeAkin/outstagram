package outstagram;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class OutstagramZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutstagramZipkinApplication.class, args);
    }

}
