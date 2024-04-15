package com.paymybudy.configuration;

import ch.qos.logback.core.net.server.Client;
import com.paymybudy.service.ClientIdentificationService;
import com.paymybudy.service.ClientUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;
import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.rememberMe;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    //We create a security filter bean for the endpoints to expose.
    //By default; all the access is blocked by Spring Security. We grant access to the URI described below.
    //AntPath allows to set the level of access control to the path level.
    //Other rules can be added: hasrole, authenticated...
    //https://www.baeldung.com/spring-security-configuring-urls

    @Autowired
    @Qualifier("clientUserDetailsService")
    private UserDetailsService clientUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //TODO--add the case to fill in the login form and auto login
        return http.csrf().disable()
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                }).formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/transactions", true) //https://www.baeldung.com/spring-redirect-after-login
                        .failureUrl("/login?error=true")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe // https://www.baeldung.com/spring-security-remember-me
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400))
                .build();
    }

    //reading login : https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(clientUserDetailsService).passwordEncoder(bCryptPasswordEncoder);

        return authenticationManagerBuilder.build();
    }


}
