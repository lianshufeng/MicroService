package com.github.microservice.auth.security.config;

import com.github.microservice.auth.security.helper.ResourcesAuthHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.annotation.AnnotationMetadataExtractor;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 角色选取控制器
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {


    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();

        //Remove the ROLE_ prefix from RoleVoter for @Secured and hasRole checks on methods
        accessDecisionManager.getDecisionVoters().stream()
                .filter(RoleVoter.class::isInstance)
                .map(RoleVoter.class::cast)
                .forEach(it -> it.setRolePrefix(""));

        return accessDecisionManager;
    }


//    customMethodSecurityMetadataSource

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return resourceAuthSecuredAnnotationSecurityMetadataSource(resourceAuthAnnotationMetadataExtractor());
    }

    //    //注解扫描器
    @Bean
    SecuredAnnotationSecurityMetadataSource resourceAuthSecuredAnnotationSecurityMetadataSource(AnnotationMetadataExtractor annotationMetadataExtractor) {
        return new ResourceAuthSecuredAnnotationSecurityMetadataSource(annotationMetadataExtractor);
    }

    //自定义注解
    @Bean
    ResourceAuthAnnotationMetadataExtractor resourceAuthAnnotationMetadataExtractor() {
        return new ResourceAuthAnnotationMetadataExtractor();
    }



}

