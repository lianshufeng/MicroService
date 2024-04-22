package com.github.microservice.core.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Arrays;
import java.util.Set;

public class ScanHelper {

    public static Set<BeanDefinition> scan(String basePackage, Class... filters) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        if (filters != null && filters.length > 0) {
            Arrays.stream(filters).forEach((it) -> {
                scanner.addIncludeFilter(new AssignableTypeFilter(Object.class));
            });
        }
        return scanner.findCandidateComponents(basePackage);
    }

}
