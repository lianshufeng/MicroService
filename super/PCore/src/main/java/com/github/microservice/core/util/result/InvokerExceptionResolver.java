package com.github.microservice.core.util.result;

import com.github.microservice.core.util.result.content.ResultException;
import com.github.microservice.core.util.result.event.ExceptionApplicationEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


/**
 * 作者：练书锋
 * 时间：2018/8/22
 * 处理统一用的系统异常
 */
@Slf4j
public class InvokerExceptionResolver implements HandlerExceptionResolver {

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        this.applicationContext.publishEvent(new ExceptionApplicationEvent(e));
        e.printStackTrace();
        log.error("exception : {}", e);
        ModelAndView mv = new ModelAndView();
        mv.addObject("state", InvokerState.Exception);
        mv.setView(new MappingJackson2JsonView());
        mv.addObject("exception", ResultException.build(e));
        return mv;
    }
}
