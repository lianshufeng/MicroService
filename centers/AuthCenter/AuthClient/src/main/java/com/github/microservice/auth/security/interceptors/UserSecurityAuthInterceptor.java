package com.github.microservice.auth.security.interceptors;

import com.github.microservice.auth.security.conf.AuthSecurityConf;
import com.github.microservice.auth.security.helper.AuthClientSecurityAuthenticationHelper;
import com.github.microservice.auth.security.helper.UserLogHelper;
import com.github.microservice.core.interceptors.UrlInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 用户安全拦截器
 */
@Component
public class UserSecurityAuthInterceptor implements UrlInterceptor {

    @Autowired
    private AuthClientSecurityAuthenticationHelper securityAuthenticationHelper;


    @Autowired
    private AuthSecurityConf userSecurityConfig;

    @Autowired
    private UserLogHelper userLogHelper;


    @Override
    public String[] addPathPatterns() {
        return this.userSecurityConfig.getNeedSecurityMethodUrl();
    }

    @Override
    public String[] excludePathPatterns() {
        return this.userSecurityConfig.getExcludeSecurityMethodUrl();
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.securityAuthenticationHelper.authenticate();
        this.userLogHelper.startLog();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.userLogHelper.endLog();
        this.securityAuthenticationHelper.release();
    }
}
