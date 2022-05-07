package com.github.microservice.core.mvc;

import com.github.microservice.core.endpoints.SuperEndpoints;
import com.github.microservice.core.util.result.InvokerExceptionResolver;
import com.github.microservice.core.util.result.InvokerResult;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MVCResponseConfiguration {


    /**
     * 通用的异常捕获
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public InvokerExceptionResolver invokerExceptionResolver() {
        return new InvokerExceptionResolver();
    }

    @Bean
    public ResponseBodyAdvice responseBodyAdvice() {
        return new UserApiResponseBodyAdvice();
    }


    @RestControllerAdvice
    public class UserApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            if (canDirect(body)) {
                return body;
            }
            //url判断过滤
            final String path = request.getURI().getPath();

            //判断忽略过滤URL
            if (StringUtils.hasText(path)) {
                for (String url : ignoreTransformUrls) {
                    if (path.indexOf(url) > -1) {
                        return body;
                    }
                }
            }
            return InvokerResult.notNull(body);
        }
    }


    //不需要转换的URL
    @Getter
    private Vector<String> ignoreTransformUrls = new Vector<>() {{
        add("actuator");
        add("manager");
        add("openapi");
        add(SuperEndpoints.DefaultEndPointName);
    }};


    private Map<Class, Boolean> bodyCacheDirect = new ConcurrentHashMap<>();

    /**
     * 能否直连
     *
     * @return
     */
    private boolean canDirect(Object body) {

        if (body == null) {
            return true;
        }

        //类型匹配
        if (body instanceof InvokerResult) {
            return true;
        }
        //类型包含状态属性
        if (body instanceof Map) {
            return ((Map) body).containsKey("state");
        }


        //缓存
        final Class bodyCls = body.getClass();

        Boolean cls = bodyCacheDirect.get(bodyCls);
        if (cls != null) {
            return cls;
        }


        Boolean exist = false;
        try {
            exist = bodyCls.getDeclaredField("state") != null;
        } catch (Exception e) {
        }
        bodyCacheDirect.put(bodyCls, exist);

        return exist;
    }

}
