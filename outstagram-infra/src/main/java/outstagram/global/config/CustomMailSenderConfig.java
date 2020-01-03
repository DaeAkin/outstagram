package outstagram.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class CustomMailSenderConfig {
    @Bean
    public JavaMailSender javaMailSender(){
        return null;
    }
}
