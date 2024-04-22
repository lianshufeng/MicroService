package com.github.microservice.core.mvc.converter;

import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

public class MSStringHttpMessageConverter extends StringHttpMessageConverter {


    public MSStringHttpMessageConverter() {
    }

    public MSStringHttpMessageConverter(Charset defaultCharset) {
        super(defaultCharset);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }
}
