package com.github.microservice.core.util.result;

import com.github.microservice.core.util.result.content.ResultException;
import com.github.microservice.core.util.result.event.ExceptionApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
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
