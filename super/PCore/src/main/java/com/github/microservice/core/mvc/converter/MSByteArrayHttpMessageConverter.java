package com.github.microservice.core.mvc.converter;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;

public class MSByteArrayHttpMessageConverter extends ByteArrayHttpMessageConverter {
    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz) ;
    }



}
