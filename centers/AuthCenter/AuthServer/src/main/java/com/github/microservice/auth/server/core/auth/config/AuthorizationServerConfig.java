package com.github.microservice.auth.server.core.auth.config;

import com.github.microservice.auth.server.core.auth.store.TokenStoreSerializationStrategy;
import com.github.microservice.auth.server.core.oauth2.authentication.OAuth2AuthenticationManager;
import com.github.microservice.auth.server.core.oauth2.service.ClientDetailsService;
import com.github.microservice.auth.server.core.oauth2.service.DefaultTokenServices;
import com.github.microservice.auth.server.core.oauth2.store.RedisTokenStore;
import com.github.microservice.auth.server.core.oauth2.store.TokenStore;
import com.github.microservice.auth.server.core.oauth2.strategy.RedisTokenStoreSerializationStrategy;
import com.github.microservice.auth.server.core.service.auth.ClientDetailsServiceImpl;
import com.github.microservice.auth.server.core.service.auth.UserDetailsServiceImpl;
import com.github.microservice.core.helper.SpringBeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {


    @Autowired
    private SpringBeanHelper springBeanHelper;


    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    @Bean
    public RedisTokenStoreSerializationStrategy redisTokenStoreSerializationStrategy() {
        return new TokenStoreSerializationStrategy();
    }


    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setSerializationStrategy(redisTokenStoreSerializationStrategy());
        return tokenStore;
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new ClientDetailsServiceImpl();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(redisTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
        oAuth2AuthenticationManager.setTokenServices(defaultTokenServices());

        return oAuth2AuthenticationManager;
    }


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