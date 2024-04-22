package com.github.microservice.auth.security.model;

import com.github.microservice.auth.client.model.OrganizationUserModel;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户令牌模型
 */
@ToString(callSuper = true)
@NoArgsConstructor
public class OrganizationUserCacheItem extends UserTokenCacheItem {

    /**
     * 缓存时间
     */
    @Getter
    private final static long cacheTime = System.currentTimeMillis();


    //缓存企业用户信息
    private Map<String, OrganizationUserCacheModel> organizationCache = new ConcurrentHashMap<>();


    /**
     * 获取企业
     *
     * @param epId
     * @return
     */
    public OrganizationUserCacheModel getOrganization(String epId) {
        return organizationCache.getOrDefault(epId, null);
    }

    /***
     * 是否存在企业
     * @param epId
     * @return
     */
    public boolean containsOrganization(String epId) {
        return organizationCache.containsKey(epId);
    }

    /**
     * 设置企业
     *
     * @param epId
     */
    public void putOrganization(String epId, OrganizationUserCacheModel userModel) {
        this.organizationCache.put(epId, userModel);
    }

    /**
     * 删除一个企业缓存
     *
     * @param epId
     * @return
     */
    public OrganizationUserModel removeOrganization(String epId) {
        return this.organizationCache.remove(epId);
    }

}
