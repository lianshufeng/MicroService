package com.github.microservice.auth.security.helper;


import com.github.microservice.auth.security.model.ResourceInfo;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源权限注解
 */

@Log
@Component
public class ResourcesAuthHelper {

    private Map<String, ResourceInfo> resourceInfoMap = new ConcurrentHashMap<>();


    /**
     * 获取资源注解的权限描述
     *
     * @return
     */
    public Collection<ResourceInfo> getResourceInfos() {
        return getResourceInfos(null);
    }


    /**
     * 获取资源信息
     *
     * @param exclude
     * @return
     */
    public Collection<ResourceInfo> getResourceInfos(Set<String> exclude) {
        Set<ResourceInfo> sets = new HashSet<>();
        resourceInfoMap.values().forEach((it) -> {
            if (exclude == null || exclude.size() == 0) {
                sets.add(it);
            } else if (!exclude.contains(it.getName())) {
                sets.add(it);
            }
        });
        return sets;
    }


    /**
     * 追加方法
     *
     * @param methodInfo
     */
    public boolean appendResourceInfo(ResourceInfo methodInfo) {
        if (StringUtils.hasText(methodInfo.getRemark())) {
            this.resourceInfoMap.put(methodInfo.getName(), methodInfo);
        } else if (!this.resourceInfoMap.containsKey(methodInfo.getName())) {
            this.resourceInfoMap.put(methodInfo.getName(), methodInfo);
        }
        return true;
    }


}
