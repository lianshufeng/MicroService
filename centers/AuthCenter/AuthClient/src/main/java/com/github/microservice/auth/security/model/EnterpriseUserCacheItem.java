package com.github.microservice.auth.security.model;

import com.github.microservice.auth.client.model.EnterpriseUserModel;
import lombok.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户令牌模型
 */
@ToString(callSuper = true)
@NoArgsConstructor
public class EnterpriseUserCacheItem extends UserTokenCacheItem {

    /**
     * 缓存时间
     */
    @Getter
    private final static long cacheTime = System.currentTimeMillis();


    //缓存企业用户信息
    private Map<String, EnterpriseUserCacheModel> enterPriseCache = new ConcurrentHashMap<>();


    /**
     * 获取企业
     *
     * @param epId
     * @return
     */
    public EnterpriseUserCacheModel getEnterprise(String epId) {
        return enterPriseCache.getOrDefault(epId, null);
    }

    /***
     * 是否存在企业
     * @param epId
     * @return
     */
    public boolean containsEnterPrise(String epId) {
        return enterPriseCache.containsKey(epId);
    }

    /**
     * 设置企业
     *
     * @param epId
     */
    public void putEnterPrise(String epId, EnterpriseUserCacheModel userModel) {
        this.enterPriseCache.put(epId, userModel);
    }

    /**
     * 删除一个企业缓存
     *
     * @param epId
     * @return
     */
    public EnterpriseUserModel removeEnterPrise(String epId) {
        return this.enterPriseCache.remove(epId);
    }

}
