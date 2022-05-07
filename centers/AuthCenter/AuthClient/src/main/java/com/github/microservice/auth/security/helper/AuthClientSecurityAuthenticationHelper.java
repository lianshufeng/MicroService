package com.github.microservice.auth.security.helper;

import com.github.microservice.auth.client.constant.AuthConstant;
import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.event.cache.EnterpriseUserCacheCreateEvent;
import com.github.microservice.auth.client.model.EnterpriseUserModel;
import com.github.microservice.auth.client.model.UserModel;
import com.github.microservice.auth.client.model.UserTokenModel;
import com.github.microservice.auth.client.service.EnterpriseUserService;
import com.github.microservice.auth.client.service.UserService;
import com.github.microservice.auth.security.annotations.ResourceAuth;
import com.github.microservice.auth.security.cache.AuthClientUserTokenCache;
import com.github.microservice.auth.security.model.*;
import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.core.util.net.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户安全助手
 */
@Slf4j
@Component
public class AuthClientSecurityAuthenticationHelper {


    //令牌的关键词
    private static final String[] UserTokenName = new String[]{"accesstoken", "accessToken", "_uToken", "uToken"};

    //企业id
    private static final String[] EnterPriseName = new String[]{"epid", "enterPriseId", "epId", "enterpriseId"};

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private AuthClientUserTokenCache authClientUserTokenCache;

    @Autowired
    private UserService userService;

    @Autowired
    private EnterpriseUserService enterpriseUserService;

    @Autowired
    private AuthHelper authHelper;


    /**
     * 权限拦截
     */
    public void authenticate() {
        //清空当前上下文
        this.authHelper.release();

        //获取用户令牌
        String uToken = getUserToken();

        //获取企业令牌
        String epId = getEnterPriseId();


        log.debug("[uToken] : {}  -> url : {} -> UA : {} -> ip : {}", uToken, httpServletRequest.getRequestURL(), httpServletRequest.getHeader("user-agent"), IPUtil.getRemoteIp(httpServletRequest));

        //设置登录用户
        setCurrentUserParm(uToken, epId);

    }


    /**
     * 设置当前用户
     *
     * @param uToken
     * @param epId
     */
    private void setCurrentUserParm(final String uToken, final String epId) {
        //缓存用户信息
        EnterpriseUserCacheItem userAutTokenCacheItem = cacheUserToken(uToken);
        if (userAutTokenCacheItem == null) {
            return;
        }

        //缓存企业信息
        this.cacheEnterPrise(userAutTokenCacheItem, epId);


        //设置spring的权限
        this.setUserAuthentication(userAutTokenCacheItem, epId);

    }

    //
//    /**
//     * 缓存企业信息
//     */
    private void cacheEnterPrise(EnterpriseUserCacheItem item, String epId) {
        //没有企业id
        if (!StringUtils.hasText(epId)) {
            return;
        }

        //有缓存则直接不缓存企业信息
        if (item.containsEnterPrise(epId)) {
            return;
        }

        ResultContent<EnterpriseUserModel> enterpriseUserModelResultContent = this.enterpriseUserService.get(epId, item.getUid());
        if (enterpriseUserModelResultContent == null || enterpriseUserModelResultContent.getState() != ResultState.Success) {
            return;
        }
        Optional.ofNullable(enterpriseUserModelResultContent.getContent()).ifPresent((enterpriseUserModel) -> {
            EnterpriseUserCacheModel newEnterpriseUserModel = new EnterpriseUserCacheModel();
            BeanUtils.copyProperties(enterpriseUserModel, newEnterpriseUserModel);
            applicationContext.publishEvent(new EnterpriseUserCacheCreateEvent(newEnterpriseUserModel));
            item.putEnterPrise(epId, newEnterpriseUserModel);
        });
    }


    /**
     * 读取并缓存
     *
     * @param uToken
     * @return
     */
    private EnterpriseUserCacheItem cacheUserToken(String uToken) {
        if (!StringUtils.hasText(uToken)) {
            return null;
        }

        //通过缓存读取
        EnterpriseUserCacheItem userAutTokenCacheItem = this.authClientUserTokenCache.get(uToken);
        if (userAutTokenCacheItem != null) {
            return userAutTokenCacheItem;
        }

        //创建新的缓存
        final EnterpriseUserCacheItem newUserAutTokenCacheItem = new EnterpriseUserCacheItem();

        //用户令牌信息
        ResultContent<UserTokenModel> userTokenModelResultContent = this.userService.queryToken(uToken);
        if (userTokenModelResultContent == null || userTokenModelResultContent.getState() != ResultState.Success) {
            log.error(" {} -> {}", uToken, userTokenModelResultContent.getState());
            return null;
        }

        Optional.ofNullable(userTokenModelResultContent.getContent()).ifPresent((userTokenModel) -> {
            BeanUtils.copyProperties(userTokenModel, newUserAutTokenCacheItem);
        });

        //用户id
        final String uid = newUserAutTokenCacheItem.getUid();

        //用户基本信息(手机号码 等)
        ResultContent<UserModel> userModelResultContent = this.userService.get(uid);
        if (userModelResultContent == null || userModelResultContent.getState() != ResultState.Success) {
            return null;
        }
        Optional.ofNullable(userModelResultContent.getContent()).ifPresent((userModel) -> {
            BeanUtils.copyProperties(userModel, newUserAutTokenCacheItem);
        });

        //设置所有的附属企业
        ResultContent<Page<String>> enterpriseUserModelPage = this.enterpriseUserService.affiliatesEnterprise(uid, PageRequest.of(0, 9999, Sort.by("createTime")));
        if (userModelResultContent == null || userModelResultContent.getState() != ResultState.Success) {
            return null;
        }
        Optional.ofNullable(enterpriseUserModelPage.getContent().getContent()).ifPresent((epIds) -> {
            newUserAutTokenCacheItem.setAffiliatesEnterprise(new HashSet<>(epIds));
        });


        this.authClientUserTokenCache.put(uToken, newUserAutTokenCacheItem);
        return newUserAutTokenCacheItem;
    }


    /**
     * 设置当前用户的权限
     */
    private void setUserAuthentication(final EnterpriseUserCacheItem cacheItem, final String epId) {
        if (cacheItem == null) {
            return;
        }

        //用户的权限
        final AuthDetails authDetails = new AuthDetails();
        BeanUtils.copyProperties(cacheItem, authDetails);

        //默认权限
        final Set<ResourceAuthModel> resourceAuthModels = new HashSet<>() {{
            add(ResourceAuthModel
                    .builder()
                    .authType(AuthType.User)
                    .name(AuthConstant.User)
                    .build());
        }};


        //没有企业id
        if (!StringUtils.hasText(epId)) {
            setAuthentication(resourceAuthModels, authDetails);
            return;
        }


        //合并用户在企业中的权限
        Optional.ofNullable(cacheItem.getEnterprise(epId)).ifPresent((enterpriseUserModel) -> {
            resourceAuthModels.addAll(enterpriseUserModel.getAuth().stream().map((it) -> {
                return ResourceAuthModel
                        .builder()
                        .authType(enterpriseUserModel.getAuthType()) // 根据类型确定是企业权限还是平台权限
                        .name(it)
                        .build();
            }).collect(Collectors.toSet()));
            BeanUtils.copyProperties(enterpriseUserModel, authDetails);
        });

        setAuthentication(resourceAuthModels, authDetails);
    }

    /**
     * 设置权限
     *
     * @param resourceAuthModels
     * @param authDetails
     */
    private void setAuthentication(final Set<ResourceAuthModel> resourceAuthModels, final AuthDetails authDetails) {
        //整合在当前企业里的权限集合
        final List<GrantedAuthority> authorities = resourceAuthModels.stream().map((it) -> {
            return new SimpleGrantedAuthority(it.getAuthType().makeAuthName(it.getName()));
        }).collect(Collectors.toList());

        //设置为当前用户
        UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(authorities);
        userAuthenticationToken.setAuthenticated(true);
        userAuthenticationToken.setDetails(authDetails);
        this.authHelper.setAuthentication(userAuthenticationToken);
    }


    /**
     * 清空当前用户身份
     */
    public void release() {
        this.authHelper.release();
    }


    /**
     * 拿到 用户令牌
     *
     * @return
     */
    public String getUserToken() {
        return getParameter(UserTokenName);
    }


    /**
     * 获取企业id
     *
     * @return
     */
    public String getEnterPriseId() {
        return getParameter(EnterPriseName);
    }


    public String getParameter(String[] names) {
        for (String tokenName : names) {
            StringBuffer uTokenBuffer = new StringBuffer();
            if (getFromCookie(tokenName, uTokenBuffer)) {
                return uTokenBuffer.toString();
            }
            if (getFromHead(tokenName, uTokenBuffer)) {
                return uTokenBuffer.toString();
            }
            if (getFromParameter(tokenName, uTokenBuffer)) {
                return uTokenBuffer.toString();
            }
        }
        return null;
    }


    /**
     * 通过cookies 拿到 token
     *
     * @return
     */
    private boolean getFromCookie(final String tokenName, final StringBuffer uTokenBuffer) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return checkParameter(cookie.getValue(), uTokenBuffer);
                }
            }
        }
        return false;
    }

    /**
     * 通过head取token
     *
     * @return
     */
    private boolean getFromHead(final String tokenName, final StringBuffer uTokenBuffer) {
        return checkParameter(httpServletRequest.getHeader(tokenName), uTokenBuffer);
    }


    /**
     * 通过参数获取用户令牌
     *
     * @param tokenName
     * @return
     */
    private boolean getFromParameter(final String tokenName, final StringBuffer uTokenBuffer) {
        return checkParameter(httpServletRequest.getParameter(tokenName), uTokenBuffer);
    }

    /**
     * 检查用户令牌,校验成功返回令牌，失败返回null
     *
     * @param parm
     * @return
     */
    private boolean checkParameter(final String parm, final StringBuffer uTokenBuffer) {
        if (StringUtils.hasText(parm)) {
            uTokenBuffer.append(parm);
            return true;
        }
        return false;
    }


}
