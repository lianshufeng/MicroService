package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.helper.TokenEventStreamHelper;
import com.github.microservice.auth.client.model.EnterpriseUserModel;
import com.github.microservice.auth.client.model.stream.EnterpriseUserStreamModel;
import com.github.microservice.auth.client.model.stream.TokenStreamModel;
import com.github.microservice.auth.client.service.EnterpriseUserService;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.client.type.AuthStreamType;
import com.github.microservice.auth.server.core.dao.EnterpriseUserDao;
import com.github.microservice.auth.server.core.dao.RoleGroupUserDao;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.auth.server.core.domain.EnterpriseUser;
import com.github.microservice.auth.server.core.domain.RoleGroupUser;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class EnterpriseUserServiceImpl implements EnterpriseUserService {

    @Autowired
    private EnterpriseUserDao enterpriseUserDao;

    @Autowired
    private RoleGroupUserDao roleGroupUserDao;

    @Autowired
    private TokenEventStreamHelper tokenEventStreamHelper;

    @Autowired
    private AuthEventStreamHelper authEventStreamHelper;

    @Autowired
    private UserDao userDao;

    @Override
    public ResultContent<Page<EnterpriseUserModel>> query(String mql, String[] fields, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(
                this.enterpriseUserDao.query(mql, fields, pageable), (enterpriseUser) -> {
                    return toModel(enterpriseUser);
                }
        ));
    }

    @Override
    public ResultContent<EnterpriseUserModel> get(String enterpriseId, String uid) {
        return ResultContent.buildContent(toModel(this.enterpriseUserDao.findByEnterpriseAndUser(Enterprise.build(enterpriseId), User.build(uid))));
    }

    @Override
    public ResultContent<Page<String>> listEnterprise(String uid, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(
                this.enterpriseUserDao.findByUser(User.build(uid), pageable),
                (it) -> {
                    return it.getEnterprise();
                }
        ));
    }

    @Override
    public ResultContent<Page<String>> affiliatesEnterprise(String uid, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.concurrent2PageModel(
                this.enterpriseUserDao.affiliatesEnterprise(uid, pageable), (enterpriseUser) -> {
                    return enterpriseUser.getEnterprise().getId();
                }));
    }


    @Override
    @Transactional
    public ResultContent<Void> update(final String enterpriseId, String[] uid) {
        log.info("更新 : enterprise:{} -> user:{}", enterpriseId, uid);
        if (!StringUtils.hasText(enterpriseId)) {
            return ResultContent.build(ResultState.EnterpriseNotNull);
        }
        if (uid == null || uid.length == 0) {
            return ResultContent.build(ResultState.UserNotNull);
        }


        //用户进行分类,一个用户存在多个角色组（用户 -> 角色组集合）
        final Map<String, List<RoleGroupUser>> roleGroupUserMap = this.roleGroupUserDao.findByEnterpriseAndUserIn(
                        Enterprise.build(enterpriseId),
                        Arrays.stream(uid)
                                .map(it -> User.build(it))
                                .collect(Collectors.toSet())
                                .toArray(new User[0]))
                .stream().collect(Collectors.groupingBy(it->it.getUser().getId()));


        //用户不存在角色组,则补充空的角色组集合
        Arrays.stream(uid)
                .filter((it) -> {
                    return this.userDao.existsById(it);
                })
                .filter((it) -> {
                    return !roleGroupUserMap.containsKey(it);
                })
                .collect(Collectors.toSet())
                .forEach((it) -> {
                    roleGroupUserMap.put(it, List.of());
                });

        //进行合并用户的角色组
        List<EnterpriseUser> enterpriseUsers = roleGroupUserMap.entrySet().stream().map((it) -> {
            return roleGroupUserToEnterpriseUser(Enterprise.build(enterpriseId), User.build(it.getKey()), it.getValue());
        }).collect(Collectors.toList());

        //更新数据
        this.enterpriseUserDao.update(enterpriseUsers);

        //发布token删除事件
        this.tokenEventStreamHelper.publish(AuthStreamType.Token, new TokenStreamModel(
                AuthEventType.Remove, null, uid));

        this.authEventStreamHelper.publish(new EnterpriseUserStreamModel(
                AuthEventType.Modify, enterpriseId, uid));

        return ResultContent.build(ResultState.Success);
    }


    private EnterpriseUser roleGroupUserToEnterpriseUser(Enterprise enterprise, User user, Collection<RoleGroupUser> roleGroupUsers) {
        final Set<String> auth = new HashSet<>();
        final Set<String> identity = new HashSet<>();
        final Set<String> roleId = new HashSet<>();
        final Set<String> roleGroupId = new HashSet<>();

        Optional.ofNullable(roleGroupUsers).ifPresent((it) -> {

            //遍历每个角色组
            it.forEach((roleGroupUser) -> {
                Optional.ofNullable(roleGroupUser.getRoleGroup()).ifPresent((roleGroup) -> {

                    //角色组
                    roleGroupId.add(roleGroup.getId());

                    //身份
                    Optional.ofNullable(roleGroup.getIdentity()).ifPresent((id) -> {
                        identity.addAll(id);
                    });

                    //角色，合并所有的权限
                    Optional.ofNullable(roleGroup.getRoles()).ifPresent((roles) -> {
                        roles.forEach((role) -> {
                            roleId.add(role.getId());
                            auth.addAll(role.getAuth());
                        });
                    });
                });
            });

        });


        return EnterpriseUser
                .builder()
                .user(user)
                .enterprise(enterprise)
                .auth(auth)
                .identity(identity)
                .roleId(roleId)
                .roleGroupId(roleGroupId)
                .build();
    }


    /**
     * @param enterpriseUser
     * @return
     */
    private EnterpriseUserModel toModel(EnterpriseUser enterpriseUser) {
        if (enterpriseUser == null) {
            return null;
        }
        final EnterpriseUserModel enterpriseUserModel = new EnterpriseUserModel();
        BeanUtils.copyProperties(enterpriseUser, enterpriseUserModel, "enterprise", "user");

        Optional.ofNullable(enterpriseUser.getEnterprise()).ifPresent((enterprise) -> {
            enterpriseUserModel.setEnterprise(enterprise.getId());
            enterpriseUserModel.setAuthType(enterprise.getAuthType());
        });

        Optional.ofNullable(enterpriseUser.getUser()).ifPresent((user) -> {
            enterpriseUserModel.setUser(user.getId());
        });

        return enterpriseUserModel;
    }


}
