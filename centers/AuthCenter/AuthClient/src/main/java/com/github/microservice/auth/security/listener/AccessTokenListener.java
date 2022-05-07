package com.github.microservice.auth.security.listener;

import com.github.microservice.auth.client.event.auth.TokenApplicationEvent;
import com.github.microservice.auth.client.model.StreamModel;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.security.cache.AuthClientUserTokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AccessTokenListener implements ApplicationListener<TokenApplicationEvent> {

    @Autowired
    private AuthClientUserTokenCache tokenCache;

    @Override
    public void onApplicationEvent(TokenApplicationEvent event) {
        StreamModel streamModel = event.getStreamModel();
        if (streamModel.getParameter().getEventType() == AuthEventType.Remove) {
            removeToken(streamModel.getParameter().getId(), streamModel.getParameter().getItems());
        }
    }

    /**
     * 删除访问令牌
     *
     * @param token
     * @param uid
     */
    private void removeToken(String token, Object[] uid) {
        Set<String> removeToken = new HashSet<>();
        Optional.ofNullable(token).ifPresent((it) -> {
            removeToken.add(it);
        });

        //取出对应用户id的令牌
        Optional.ofNullable(uid).ifPresent((userIds) -> {
            this.tokenCache.findUserByUid(Arrays.asList(uid).toArray(new String[0])).values().forEach((enterpriseUserCacheItems) -> {
                enterpriseUserCacheItems.forEach((item) -> {
                    removeToken.add(item.getAccessToken());
                });
            });
        });


        //删除缓存
        removeToken.forEach((uToken) -> {
            this.tokenCache.remove(uToken);
        });

    }
}
