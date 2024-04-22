package com.github.microservice.core.delegate;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DelegateHelper {


    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    private Method getMappingForMethod;

    /**
     * 需要过滤的方法
     */
    private final static List<String> FilterMethods = new ArrayList<>() {{
        add("java.lang.Object.equals(java.lang.Object)");
        add("java.lang.Object.finalize()");
        add("java.lang.Object.toString()");
        add("java.lang.Object.getClass()");
        add("java.lang.Object.notifyAll()");
        add("java.lang.Object.hashCode()");
        add("java.lang.Object.wait()");
        add("java.lang.Object.notify()");
        add("java.lang.Object.wait(long)");
        add("java.lang.Object.wait(long,int)");
        add("java.lang.Object.clone()");
    }};


    private final static List<String> FilterMethodsName = new ArrayList<>() {{
        add("public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException");
        add("public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException");
        add("public final void java.lang.Object.wait() throws java.lang.InterruptedException");
        add("public boolean java.lang.Object.equals(java.lang.Object)");
        add("public java.lang.String java.lang.Object.toString()");
        add("public native int java.lang.Object.hashCode()");
        add("public final native java.lang.Class<?> java.lang.Object.getClass()");
        add("public final native void java.lang.Object.notify()");
        add("public final native void java.lang.Object.notifyAll()");
    }};


    final ClassPool pool = ClassPool.getDefault();

    private BeanDefinitionRegistry registry;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        Arrays.stream(requestMappingHandlerMapping.getClass().getDeclaredMethods()).filter(it -> it.getName().equals("getMappingForMethod")).findFirst().ifPresent((method) -> {
            method.setAccessible(true);
            getMappingForMethod = method;
        });
    }


    @SneakyThrows
    private RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        Object ret = getMappingForMethod.invoke(requestMappingHandlerMapping, method, handlerType);
        return ret == null ? null : (RequestMappingInfo) ret;
    }


    /**
     * 查询代理方法
     *
     * @param sourceMethod
     * @param delegateInterfaces
     * @return
     */
    private Method findDelegateMethod(final Method sourceMethod, final Class[] delegateInterfaces) {
        for (Class delegateInterface : List.of(delegateInterfaces)) {
            for (Method delegateMethod : delegateInterface.getMethods()) {
                if (sourceMethod.getName().equals(delegateMethod.getName()) && Arrays.equals(sourceMethod.getParameterTypes(), delegateMethod.getParameterTypes())) {
                    return delegateMethod;
                }
            }
        }
        return null;
    }


    @SneakyThrows
    public void delegateMapping(final Object bean) {
        final Class cls = bean.getClass();
        //取出代理的接口
        final DelegateMapping delegateMapping = (DelegateMapping) cls.getAnnotation(DelegateMapping.class);
        final Class[] delegateInterfaces = delegateMapping.types();

        //过滤特殊方法
        List<Method> methods = List.of(cls.getMethods()).stream().filter(it -> !FilterMethodsName.contains(it.toGenericString())).collect(Collectors.toList());

        //过滤有注解的方法
        methods = methods.stream().filter(it -> !(it.isAnnotationPresent(RequestMapping.class) || it.isAnnotationPresent(PostMapping.class) || it.isAnnotationPresent(GetMapping.class))).collect(Collectors.toList());

        for (Method method : methods) {
            // 从代理类里匹配方法名和参数
            Method delegateMethod = findDelegateMethod(method, delegateInterfaces);

            //            String[] beanNames = applicationContext.getBeanNamesForType(delegateMethod.getDeclaringClass());
//            if (beanNames == null || beanNames.length == 0) {
//                continue;
//            }
//            HandlerMethod handlerMethod = new HandlerMethod(beanNames[0], this.applicationContext, delegateMethod);

            //拷贝注解:


            HandlerMethod handlerMethod = new HandlerMethod(bean, delegateMethod);
            RequestMappingInfo mappingInfo = getMappingForMethod(handlerMethod.getMethod(), handlerMethod.getBeanType());
            requestMappingHandlerMapping.registerMapping(mappingInfo, bean, method);
            log.info("DelegateMapping : - {} ", mappingInfo);
        }


    }


    /**
     * 更新class的字节码
     */
//    @SneakyThrows
//    public Object update(final Class cls) {
//        //取出代理的接口
//        final DelegateMapping delegateMapping = (DelegateMapping) cls.getAnnotation(DelegateMapping.class);
//        final Class[] delegateInterfaces = delegateMapping.types();
//
//        //扫描
//        CtClass ctSource = scanClasses(cls, delegateInterfaces);
//        ctSource.setName(ctSource.getName() + "_Delegate");
//
//        final Class newClass = ctSource.toClass();
//        final Object obj = newClass.getDeclaredConstructor(null).newInstance();
//
//        //实例化并注入依赖对象
//        this.springBeanHelper.injection(obj);
//
//        return obj;
//    }
    @SneakyThrows
    private CtClass scanClasses(final Class source, final Class[] delegateInterfaces) {

        //原class
        final CtClass ctSource = pool.get(source.getName());

        //目标class
        List<CtClass> delegates = Arrays.stream(delegateInterfaces).map(it -> {
            try {
                return pool.get(it.getName());
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        //整理Mapping上接口所映射的所有方法
        final Map<String, CtMethod> delegatesMethods = new HashMap<>();
        delegates.stream().map(it -> List.of(it.getMethods())).collect(Collectors.toList()).forEach(methodList -> {
            //过滤不需要处理的方法,以及方法名相同
            methodList.stream().filter(it -> !FilterMethods.contains(it.getLongName())).forEach((it) -> {
                delegatesMethods.put(it.getLongName(), it);
            });
        });
        //代理方法集合
        final Collection<CtMethod> delegatesMethodCollection = delegatesMethods.values();


        //原方法集合
        final List<CtMethod> sourcesMethods = Arrays.stream(ctSource.getMethods()).filter(it -> !FilterMethods.contains(it.getLongName())).collect(Collectors.toList());


        //对比原方法和代理方法
        for (CtMethod sourceMethod : sourcesMethods) {
            for (CtMethod delegateMethod : delegatesMethodCollection) {
                if (sourceMethod.getName().equals(delegateMethod.getName()) && Arrays.equals(sourceMethod.getParameterTypes(), delegateMethod.getParameterTypes())) {
                    updateMappingBytecodes(sourceMethod, delegateMethod);
                }
            }
        }

        return ctSource;
    }

    /**
     * 更新字节码
     */
    @SneakyThrows
    private void updateMappingBytecodes(CtMethod sourceMethod, CtMethod delegateMethod) {

        MethodInfo srcInfo = delegateMethod.getMethodInfo();
        MethodInfo destInfo = sourceMethod.getMethodInfo();

        // 遍历被复制的方法的属性
        for (Object attribute : srcInfo.getAttributes()) {
            if (attribute instanceof AnnotationsAttribute || attribute instanceof ParameterAnnotationsAttribute) {
                AnnotationsAttribute annotations = (AnnotationsAttribute) attribute;
                AnnotationsAttribute newAnnotations = new AnnotationsAttribute(destInfo.getConstPool(), annotations.getName());
                newAnnotations.setAnnotations(annotations.getAnnotations());
                destInfo.addAttribute(newAnnotations);
            }
        }


//        delegateMethod.getMethodInfo().getAttributes().stream().filter(it -> it instanceof AnnotationsAttribute || it instanceof ParameterAnnotationsAttribute).forEach(it -> {
//            sourceMethod.getMethodInfo().addAttribute(it);
//        });


    }


}
