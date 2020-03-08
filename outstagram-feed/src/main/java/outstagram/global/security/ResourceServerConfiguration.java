package outstagram.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void configure(HttpSecurity http) throws Exception{

//        http
//                .authorizeRequests()
////                .antMatchers(HttpMethod.GET, "/feed/**").permitAll()
//                .anyRequest().access("#oauth2.hasScope('webclient')")
//                .anyRequest().access("#oauth2.hasScope('mobileclient')");


        if(!activeProfile.equals("test")) {
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/follow/**").permitAll()
                    .anyRequest().access("#oauth2.hasScope('webclient')")
                    .anyRequest().access("#oauth2.hasScope('mobileclient')");
        } else {
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/**").permitAll()
                    .anyRequest().access("#oauth2.hasScope('webclient')")
                    .anyRequest().access("#oauth2.hasScope('mobileclient')");
        }






    }
}