package com.github.microservice.auth.server.core.auth.config;

import com.github.microservice.auth.server.core.auth.endpoint.AuthTokenEndpoint;
import com.github.microservice.auth.server.core.auth.store.TokenStoreSerializationStrategy;
import com.github.microservice.core.helper.SpringBeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;


@Configuration
@EnableAuthorizationServer
@SuppressWarnings("deprecation")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SpringBeanHelper springBeanHelper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


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
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();// new BCryptPasswordEncoder();
        return new BCryptPasswordEncoder();
    }


    public TokenEndpoint tokenEndpoint(AuthorizationServerEndpointsConfigurer endpoints) {
        AuthTokenEndpoint tokenEndpoint = new AuthTokenEndpoint();
        tokenEndpoint.setClientDetailsService(endpoints.getClientDetailsService());
        tokenEndpoint.setProviderExceptionHandler(endpoints.getExceptionTranslator());
        tokenEndpoint.setTokenGranter(endpoints.getTokenGranter());
        tokenEndpoint.setOAuth2RequestFactory(endpoints.getOAuth2RequestFactory());
        tokenEndpoint.setOAuth2RequestValidator(endpoints.getOAuth2RequestValidator());
        tokenEndpoint.setAllowedRequestMethods(endpoints.getAllowedTokenEndpointRequestMethods());
        return tokenEndpoint;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // 集成websecurity认证
        endpoints.authenticationManager(authenticationManager);

        // 注册redis令牌仓库
        endpoints.tokenStore(redisTokenStore());

        //用户身份校验
        endpoints.userDetailsService(userDetailsService);


        //自定义令牌端
        TokenEndpoint tokenEndpoint = tokenEndpoint(endpoints);
        springBeanHelper.injection(tokenEndpoint);
        springBeanHelper.registerSingleton("tokenEndpoint", tokenEndpoint);

    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许通过form提交客户端认证信息(client_id,client_secret),默认为basic方式认证
//        security.allowFormAuthenticationForClients();
        // "/oauth/check_token"端点默认不允许访问
//        security.checkTokenAccess("isAuthenticated()");
        // "/oauth/token_key"断点默认不允许访问
//        security.tokenKeyAccess("isAuthenticated()");
        //允许表单认证
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
        // 配置密码编码器
        security.passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }


}