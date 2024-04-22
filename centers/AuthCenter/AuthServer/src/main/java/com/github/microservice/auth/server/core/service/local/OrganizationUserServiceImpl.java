package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.helper.TokenEventStreamHelper;
import com.github.microservice.auth.client.model.OrganizationUserModel;
import com.github.microservice.auth.client.model.stream.OrganizationUserStreamModel;
import com.github.microservice.auth.client.model.stream.TokenStreamModel;
import com.github.microservice.auth.client.service.OrganizationUserService;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.client.type.AuthStreamType;
import com.github.microservice.auth.server.core.dao.OrganizationUserDao;
import com.github.microservice.auth.server.core.dao.RoleGroupUserDao;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.domain.Organization;
import com.github.microservice.auth.server.core.domain.OrganizationUser;
import com.github.microservice.auth.server.core.domain.RoleGroupUser;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Primary
public class OrganizationUserServiceImpl implements OrganizationUserService {

    @Autowired
    private OrganizationUserDao organizationUserDao;

    @Autowired
    private RoleGroupUserDao roleGroupUserDao;

    @Autowired
    private TokenEventStreamHelper tokenEventStreamHelper;

    @Autowired
    private AuthEventStreamHelper authEventStreamHelper;

    @Autowired
    private UserDao userDao;

    @Override
    public ResultContent<Page<OrganizationUserModel>> query(String mql, String[] fields, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(this.organizationUserDao.query(mql, fields, pageable), (organizationUser) -> {
            return toModel(organizationUser);
        }));
    }

    @Override
    public ResultContent<OrganizationUserModel> get(String organizationId, String uid) {
        return ResultContent.buildContent(toModel(this.organizationUserDao.findByOrganizationAndUser(Organization.build(organizationId), User.build(uid))));
    }

    @Override
    public ResultContent<Page<String>> listOrganization(String uid, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(this.organizationUserDao.findByUser(User.build(uid), pageable), (it) -> {
            return it.getOrganization();
        }));
    }

    @Override
    public ResultContent<Page<String>> affiliatesOrganization(String uid, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.concurrent2PageModel(this.organizationUserDao.affiliatesOrganization(uid, pageable), (organizationUser) -> {
            return organizationUser.getOrganization().getId();
        }));
    }


    @Override
    @Transactional
    public ResultContent<Void> update(final String organizationId, String[] uid) {
        log.info("更新 : organization:{} -> user:{}", organizationId, uid);
        if (!StringUtils.hasText(organizationId)) {
            return ResultContent.build(ResultState.OrganizationNotExist);
        }
        if (uid == null || uid.length == 0) {
            return ResultContent.build(ResultState.UserNotNull);
        }


        //用户进行分类,一个用户存在多个角色组（用户 -> 角色组集合）
        final Map<String, List<RoleGroupUser>> roleGroupUserMap = this.roleGroupUserDao.findByOrganizationAndUserIn(Organization.build(organizationId), Arrays.stream(uid).map(it -> User.build(it)).collect(Collectors.toSet()).toArray(new User[0])).stream().collect(Collectors.groupingBy(it -> it.getUser().getId()));


        //用户不存在角色组,则补充空的角色组集合
        Arrays.stream(uid).filter((it) -> {
            return this.userDao.existsById(it);
        }).filter((it) -> {
            return !roleGroupUserMap.containsKey(it);
        }).collect(Collectors.toSet()).forEach((it) -> {
            roleGroupUserMap.put(it, List.of());
        });

        //进行合并用户的角色组
        List<OrganizationUser> organizationUsers = roleGroupUserMap.entrySet().stream().map((it) -> {
            return roleGroupUserToOrganizationUser(Organization.build(organizationId), User.build(it.getKey()), it.getValue());
        }).collect(Collectors.toList());

        //更新数据
        this.organizationUserDao.update(organizationUsers);

        //发布token删除事件
        this.tokenEventStreamHelper.publish(AuthStreamType.Token, new TokenStreamModel(AuthEventType.Remove, null, uid));

        this.authEventStreamHelper.publish(new OrganizationUserStreamModel(AuthEventType.Modify, organizationId, uid));

        return ResultContent.build(ResultState.Success);
    }


    private OrganizationUser roleGroupUserToOrganizationUser(Organization organization, User user, Collection<RoleGroupUser> roleGroupUsers) {
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


        return OrganizationUser.builder().user(user).organization(organization).auth(auth).identity(identity).roleId(roleId).roleGroupId(roleGroupId).build();
    }


    /**
     * @param organizationUser
     * @return
     */
    private OrganizationUserModel toModel(OrganizationUser organizationUser) {
        if (organizationUser == null) {
            return null;
        }
        final OrganizationUserModel organizationUserModel = new OrganizationUserModel();
        BeanUtils.copyProperties(organizationUser, organizationUserModel, "organization", "user");

        Optional.ofNullable(organizationUser.getOrganization()).ifPresent((organization) -> {
            organizationUserModel.setOrganization(organization.getId());
            organizationUserModel.setAuthType(organization.getAuthType());
        });

        Optional.ofNullable(organizationUser.getUser()).ifPresent((user) -> {
            organizationUserModel.setUser(user.getId());
        });

        return organizationUserModel;
    }


}
