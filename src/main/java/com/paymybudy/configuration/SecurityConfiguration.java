package com.paymybudy.configuration;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class SecurityConfiguration {
    //We create a security filter bean for the endpoints to expose.
    //By default; all the access is blocked by Spring Security. We grant access to the URI described below.
    //AntPath allows to set the level of access control to the path level.
    //Other rules can be added: hasrole, authenticated...
    //https://www.baeldung.com/spring-security-configuring-urls

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.antMatcher("/**").authorizeRequest().anyRequest().authenticated();
        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/transactions", true) //https://www.baeldung.com/spring-redirect-after-login
                        .permitAll());
        //TODO -- Remember me
        //reading login : https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
        //remember me : https://docs.spring.io/spring-security/reference/servlet/authentication/rememberme.html

        return http
                .csrf().disable() // allowed testing
                .authorizeHttpRequests()
                        .anyRequest().authenticated()
                .and().build();
                /**.authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/"))
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/newconnection", "GET"))
                        .permitAll()) //TODO -- to build the User profile so to add authentication based on hasRole()
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/newconnection", "POST"))
                        .permitAll())
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/transactions"))
                        .permitAll()) //TODO -- to build the User profile so to add authentication based on hasRole()
                .build();
                 */

    }




    /*@Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.builder()
                .username("user")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER", "ADMIN")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(admin);
        return users;
    }

     */
}
