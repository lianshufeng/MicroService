package com.github.microservice.auth.security.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import com.github.microservice.core.mvc.MVCResponseConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 不拦截web请求
 */
@Order
@EnableWebSecurity
@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) throws Exception {
        //排除不拦截的静态资源
        web.ignoring().antMatchers("/" + MVCConfiguration.StaticResources + "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .logout()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }


}



