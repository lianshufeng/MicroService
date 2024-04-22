package com.github.microservice.auth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Import({WebSecurityConfigurer.class, MethodSecurityConfig.class})
@ComponentScan({"com.github.microservice.auth.security"})
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(it -> it.disable())
                .logout(it -> it.disable())
                .formLogin(it -> it.disable())
                .httpBasic(withDefaults());
        return http.build();
    }

}
