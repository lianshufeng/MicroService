package com.github.microservice.auth.security.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.microservice.auth.security.model.EnterpriseUserCacheItem;
import com.github.microservice.auth.security.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthClientUserTokenCache {

    private final static String CacheName = "UserToken";

    @Autowired
    private AuthClientUserTokenCache me;


    //缓存
    private Cache<String, EnterpriseUserCacheItem> cache;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        //不影响默认的配置
        Caffeine caffeine = Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(99999);
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);
        this.cache = (Cache) caffeineCacheManager.getCache(CacheName).getNativeCache();
        this.cache.cleanUp();
    }

    /**
     * 重置缓存
     */
    public void cleanAllCache() {
        if (this.cache != null) {
            this.cache.asMap().clear();
            log.info("Rest All UserToken Cache");
        }
    }


    /**
     * 用户缓存 ，用户中心的id
     */
    public void cleanUserCache(String... uid) {
        Map<String, Set<EnterpriseUserCacheItem>> items = this.findUserByUid(uid);
        if (items == null) {
            return;
        }
        Set<String> uTokens = new HashSet<>();
        items.values().forEach((it) -> {
            uTokens.addAll(it.stream().map((item) -> {
                return item.getAccessToken();
            }).collect(Collectors.toSet()));
        });

        //删除缓存
        uTokens.forEach((it) -> {
            me.remove(it);
        });

    }


    /**
     * 通过用户id查询到缓存里的对象
     *
     * @param uid
     */
    public Map<String, Set<EnterpriseUserCacheItem>> findUserByUid(String... uid) {
        if (uid == null || uid.length == 0) {
            return null;
        }
        //缓存map
        final Map<String, EnterpriseUserCacheItem> cacheMap = this.cache.asMap();
        Map<String, Set<EnterpriseUserCacheItem>> ret = new HashMap<>();
        for (String u : uid) {
            Set<EnterpriseUserCacheItem> userAutTokenCacheItems = ret.get(u);
            if (userAutTokenCacheItems == null) {
                userAutTokenCacheItems = new HashSet<EnterpriseUserCacheItem>();
                ret.put(u, userAutTokenCacheItems);
            }

            // 因为 NullValue 不能用 stream
            for (Object item : cacheMap.entrySet()) {
                Map.Entry entry = (Map.Entry) item;
                Object value = entry.getValue();
                if (value != null && value instanceof EnterpriseUserCacheItem) {
                    EnterpriseUserCacheItem cacheItem = (EnterpriseUserCacheItem) value;
                    if (u.equals(cacheItem.getUid())) {
                        userAutTokenCacheItems.add(cacheItem);
                    }
                }
            }
        }
        return ret;
    }


    /**
     * 取出缓存
     */
    public EnterpriseUserCacheItem get(String uToken) {
        if (!StringUtils.hasText(uToken)) {
            return null;
        }
        EnterpriseUserCacheItem item = this.me.readCache(uToken);
        if (item == null) {
            return null;
        }
        //缓存超时
        if (TimeUtil.getTime() > item.getExpireTime()) {
            this.me.remove(uToken);
            return null;
        }
        return item;
    }


    /**
     * 设置缓存
     *
     * @return
     */
//    @CachePut(value = CacheName, key = "#uToken")
    public EnterpriseUserCacheItem put(String uToken, EnterpriseUserCacheItem t) {
        log.info("Put Cache : {}", t.getAccessToken());
        this.cache.put(uToken, t);
        return t;
    }

    /**
     * 取出缓存
     */
//    @Cacheable(value = CacheName, key = "#uToken")
    public EnterpriseUserCacheItem readCache(String uToken) {
        Object item = this.cache.getIfPresent(uToken);
        if (item instanceof EnterpriseUserCacheItem) {
            return (EnterpriseUserCacheItem) item;
        }
        log.info("Miss Cache : {}", uToken);
        return null;
    }

    /**
     * 删除缓存
     */
//    @CacheEvict(value = CacheName, key = "#uToken")
    public void remove(String uToken) {
        log.info("remove Cache : " + uToken);
        this.cache.asMap().remove(uToken);
    }


}
