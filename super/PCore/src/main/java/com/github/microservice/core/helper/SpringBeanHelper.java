package com.github.microservice.core.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringBean的助手，动态加载spring的bean类
 */
public class SpringBeanHelper {


    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Autowired
    private ApplicationContext applicationContext;


    private AutowiredAnnotationBeanPostProcessor aabpp;


    private CommonAnnotationBeanPostProcessor cabpp;

    private Map<String, String> beanNameCache = new ConcurrentHashMap<>();


    @Autowired
    private void init(ConfigurableApplicationContext applicationContext) {
        aabpp = (AutowiredAnnotationBeanPostProcessor) applicationContext.getBean(AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME);
        cabpp = (CommonAnnotationBeanPostProcessor) applicationContext.getBean(AnnotationConfigUtils.COMMON_ANNOTATION_PROCESSOR_BEAN_NAME);
    }


    /**
     * 注入依赖对象
     *
     * @param o
     */
    public void update(String beanName, Object o) {

        //如果已经存在spring容器里则先删除掉
        if (this.beanFactory.containsBean(beanName)) {
            remove(beanName);
        }

        //注入bean对象
        injection(o);

        //注册到spring的容器里
        registerSingleton(beanName, o);

    }


    public void registerSingleton(String beanName, Object o) {
        //注册到Spring的容器中
        this.beanFactory.registerSingleton(beanName, o);
    }


    /**
     * 注入bean对象
     *
     * @param o
     */
    public void injection(Object o) {
        //完成自动注入
        // @Autowired
        this.aabpp.processInjection(o);

        // @Resource
        this.cabpp.postProcessProperties(null, o, null);

        // @PostConstruct
        this.cabpp.postProcessBeforeInitialization(o, null);
    }


    /**
     * 删除注册的bean对象
     *
     * @param beanName
     */
    public void remove(String beanName) {
        DefaultSingletonBeanRegistry defaultSingletonBeanRegistry = (DefaultSingletonBeanRegistry) beanFactory;
        defaultSingletonBeanRegistry.destroySingleton(beanName);
    }


    /**
     * 取对象的BeanName
     *
     * @param o
     * @return
     */
    public String getBeanName(Object o) {
        Class<?> cls = o.getClass();
        String beanName = beanNameCache.get(cls.getSimpleName());
        if (beanName == null) {
            for (String name : this.applicationContext.getBeansOfType(cls).keySet()) {
                return name;
            }
        }
        return null;
    }

}
