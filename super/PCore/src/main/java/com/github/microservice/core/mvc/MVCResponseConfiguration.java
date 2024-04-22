package com.github.microservice.core.mvc;

import com.github.microservice.core.util.result.InvokerExceptionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
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


//    @Bean
//    public ResponseBodyAdvice responseBodyAdvice() {
//        return new UserApiResponseBodyAdvice();
//    }


//    @RestControllerAdvice
//    public class UserApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {
//
//        @Override
//        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//            return true;
//        }
//
//        @Override
//        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//            return body;
//        }
//    }


}
