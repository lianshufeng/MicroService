package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.model.LoginTokenModel;
import com.github.microservice.auth.security.model.UserAuthenticationToken;
import com.github.microservice.auth.server.core.model.CreateUserTokenModel;
import com.github.microservice.auth.server.core.oauth2.authentication.OAuth2Authentication;
import com.github.microservice.auth.server.core.oauth2.request.OAuth2Request;
import com.github.microservice.auth.server.core.oauth2.store.TokenStore;
import com.github.microservice.auth.server.core.oauth2.token.DefaultOAuth2AccessToken;
import com.github.microservice.auth.server.core.oauth2.token.DefaultOAuth2RefreshToken;
import com.github.microservice.auth.server.core.oauth2.token.OAuth2AccessToken;
import com.github.microservice.auth.server.core.oauth2.token.OAuth2RefreshToken;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Primary
public class TokenService {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DBHelper dbHelper;


    /**
     * 删除用户令牌
     * @param refreshToken
     * @return
     */
    public boolean removeTokenFromRefreshToken(final String refreshToken) {
        OAuth2RefreshToken oAuth2RefreshToken = this.tokenStore.readRefreshToken(refreshToken);
        if (oAuth2RefreshToken == null) {
            return false;
        }
        this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
        this.tokenStore.removeRefreshToken(oAuth2RefreshToken);
        return true;
    }

    /**
     * 删除用户令牌
     */
    public boolean removeTokenFromAccessToken(final String accessToken) {
        OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            return false;
        }
        this.tokenStore.removeAccessToken(oAuth2AccessToken);
        Optional.ofNullable(this.tokenStore.readRefreshToken(oAuth2AccessToken.getRefreshToken().getValue())).ifPresent(it -> {
            this.tokenStore.removeRefreshToken(it);
        });
        return true;
    }


    /**
     * 创建用户令牌
     */
    public LoginTokenModel createUserToken(final CreateUserTokenModel createUserTokenModel) {
        OAuth2Request storedRequest = new OAuth2Request(createUserTokenModel.getClientId());
        final UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(null) {
            @Override
            public Object getPrincipal() {
                return createUserTokenModel.getUserId();
            }
        };

        OAuth2Authentication userAuthentication = new OAuth2Authentication(storedRequest, userAuthenticationToken);


        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedRequest, userAuthentication);
        //刷新令牌
        DefaultOAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(TokenUtil.createAndHash());
        //访问令牌
        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(TokenUtil.createAndHash());
        oAuth2AccessToken.setRefreshToken(oAuth2RefreshToken);
        oAuth2AccessToken.setExpiration(new Date(dbHelper.getTime() + createUserTokenModel.getAccessTokenTimeOut() * 1000));

        tokenStore.storeAccessToken(oAuth2AccessToken, oAuth2Authentication);
        tokenStore.storeRefreshToken(oAuth2RefreshToken, oAuth2Authentication);

        LoginTokenModel loginTokenModel = new LoginTokenModel();
        loginTokenModel.setAccess_token(oAuth2AccessToken.getValue());
        loginTokenModel.setRefresh_token(oAuth2RefreshToken.getValue());
        loginTokenModel.setToken_type(oAuth2AccessToken.getTokenType());
        loginTokenModel.setScope(null);
        loginTokenModel.setExpires_in(oAuth2AccessToken.getExpiresIn());

        return loginTokenModel;
    }





}
