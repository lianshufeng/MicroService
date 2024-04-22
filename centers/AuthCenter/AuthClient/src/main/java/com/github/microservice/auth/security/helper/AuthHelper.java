package com.github.microservice.auth.security.helper;

import com.github.microservice.auth.security.model.AuthDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthHelper {


    @Autowired
    private UserLogHelper userLogHelper;

    @Autowired
    private AuthClientSecurityAuthenticationHelper authClientSecurityAuthenticationHelper;


    /**
     * 获取当前用户
     *
     * @return
     */
    public AuthDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object details = authentication.getDetails();
        if (details instanceof AuthDetails) {
            return (AuthDetails) details;
        }
        return null;
    }


    /**
     * 拿到 用户令牌
     *
     * @return
     */
    public String getUserToken() {
        return this.authClientSecurityAuthenticationHelper.getUserToken();
    }


    /**
     * 获取企业id
     *
     * @return
     */
    public String getOrgId() {
        return this.authClientSecurityAuthenticationHelper.getOrgId();
    }




    /**
     * 记录日志
     *
     * @param key
     * @param value
     */
    public void log(String key, Object value) {
        this.userLogHelper.log(key, value);
    }


    /**
     * 设置权限
     *
     * @param authentication
     */
    protected void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    /**
     * 释放权限
     */
    protected void release() {
        SecurityContextHolder.clearContext();
    }


}
