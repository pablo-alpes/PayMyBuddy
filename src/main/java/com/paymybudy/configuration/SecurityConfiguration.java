package com.paymybudy.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {
    //We create a security filter bean for the endpoints to expose.
    //By default; all the access is blocked by Spring Security. We grant access to the URI described below.
    //AntPath allows to set the level of access control to the path level.
    //Other rules can be added: hasrole, authenticated...
    //https://www.baeldung.com/spring-security-configuring-urls

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/transactions", true) //https://www.baeldung.com/spring-redirect-after-login
                        .permitAll());
        //TODO -- Remember me
        //reading login : https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
        //remember me : https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html
        return http.authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/"))
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/transactions"))
                        .permitAll()) //TODO -- to build the User profile so to add authentication based on hasRole()
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/error"))
                        .permitAll())
                .build();
    }
}
