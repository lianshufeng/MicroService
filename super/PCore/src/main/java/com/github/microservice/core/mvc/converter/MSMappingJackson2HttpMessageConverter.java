package com.github.microservice.core.mvc.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.microservice.core.util.result.InvokerResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

public class MSMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {


    private final static String[] invokerResultField = new String[]{"state", "content"};


    @Getter
    private Vector<String> ignoreUrls = new Vector() {{
        add(("^/actuator/.*"));// /v3/api-docs
        add(("^/v3/.*"));// /v3/api-docs
        add((".*manager.*")); // **manager**
        add(("/error")); // /error
    }};


    @Override
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        super.setObjectMapper(objectMapper);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.writeInternal(invokerResult(object), outputMessage);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.writeInternal(invokerResult(object), type, outputMessage);
    }


    // 是否要处理成标准响应对象
    private Object invokerResult(Object body) {

        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        if (attribs == null) {
            return body;
        }

        // 过滤忽略的url
        final HttpServletRequest request = ((ServletRequestAttributes) attribs).getRequest();

        final String path = request.getRequestURI();
        if (StringUtils.hasText(path)) {
            for (String expression : ignoreUrls) {
                if (Pattern.compile(expression).matcher(path).find()) {
                    return body;
                }
            }
        }

        //对象匹配
        if (body instanceof InvokerResult) {
            return body;
        }

        //Map中是否包含属性
        if (body instanceof Map && exitsInvokerResultField(it -> ((Map) body).containsKey(it))) {
            return body;
        }

        //自定义对象中包含属性
        if (exitsInvokerResultField(it -> ReflectionUtils.findField(body.getClass(), it) != null)) {
            return body;
        }

        return InvokerResult.notNull(body);
    }

    /**
     * 判断是否存在标准接口的资源
     *
     * @param consumer
     */
    private boolean exitsInvokerResultField(Consumer consumer) {
        for (String field : invokerResultField) {
            if (!consumer.accept(field)) {
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    private interface Consumer {

        boolean accept(String field);

    }


}
