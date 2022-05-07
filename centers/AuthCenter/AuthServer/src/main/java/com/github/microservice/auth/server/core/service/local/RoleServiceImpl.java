package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.model.RoleGroupModel;
import com.github.microservice.auth.client.model.RoleGroupUserModel;
import com.github.microservice.auth.client.model.RoleModel;
import com.github.microservice.auth.client.model.stream.RoleGroupStreamModel;
import com.github.microservice.auth.client.model.stream.RoleGroupUserStreamModel;
import com.github.microservice.auth.client.model.stream.RoleStreamModel;
import com.github.microservice.auth.client.service.EnterpriseUserService;
import com.github.microservice.auth.client.service.RoleService;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.server.core.dao.*;
import com.github.microservice.auth.server.core.domain.*;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleGroupDao roleGroupDao;

    @Autowired
    private RoleGroupUserDao roleGroupUserDao;

    @Autowired
    private EnterpriseUserService enterpriseUserService;

    @Autowired
    private AuthEventStreamHelper authEventStreamHelper;

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private DBHelper dbHelper;

    private ResultContent<String> addRole(RoleModel roleModel) {
        //新增校验
        if (!StringUtils.hasText(roleModel.getEnterpriseId())) {
            return ResultContent.build(ResultState.EnterpriseNotNull);
        }
        if (!this.enterpriseDao.existsById(roleModel.getEnterpriseId())) {
            return ResultContent.build(ResultState.EnterpriseNotExist);
        }

        if (!StringUtils.hasText(roleModel.getName())) {
            return ResultContent.build(ResultState.RoleNameNotNull);
        }
        if (this.roleDao.existsByEnterpriseAndName(Enterprise.build(roleModel.getEnterpriseId()), roleModel.getName())) {
            return ResultContent.build(ResultState.RoleExists);
        }

        //新增需要设置企业,创建角色必须包含企业
        Role role = new Role();
        role.setEnterprise(Enterprise.build(roleModel.getEnterpriseId()));
        return this.saveRole(roleModel, role);
    }

    private ResultContent<String> modifyRole(RoleModel roleModel) {
        Role role = this.roleDao.findTop1ById(roleModel.getId());
        if (role == null) {
            return ResultContent.build(ResultState.RoleNotExists);
        }
        //目标角色名存在
        if (this.roleDao.existsByEnterpriseAndName(role.getEnterprise(), roleModel.getName())) {
            return ResultContent.build(ResultState.RoleExists);
        }

        if (StringUtils.hasText(roleModel.getEnterpriseId()) && !role.getEnterprise().getId().equals(roleModel.getEnterpriseId())) {
            return ResultContent.build(ResultState.RoleEnterpriseDoNotUpdate);
        }
        return this.saveRole(roleModel, role);
    }

    private ResultContent<String> saveRole(RoleModel roleModel, Role role) {
        //忽略要过滤的字段
        Set<String> ignoreProperties = new HashSet<>() {{
            add("id");
        }};
        //过滤拷贝为null的字段
        BeanUtil.getNullPropertyNames(roleModel, ignoreProperties);
        //拷贝为数据库实体
        BeanUtils.copyProperties(roleModel, role, ignoreProperties.toArray(new String[0]));


        this.dbHelper.saveTime(role);
        this.roleDao.save(role);

        //取出角色id
        final String roleId = role.getId();
        //仅修改角色才需要更新用户
        if (StringUtils.hasText(roleModel.getId())) {
            this.updateEnterpriseUserFromRole(Set.of(roleModel.getId()));
        }

        //发布事件
        this.authEventStreamHelper.publish(new RoleStreamModel(
                StringUtils.hasText(roleModel.getId()) ? AuthEventType.Modify : AuthEventType.Add,
                roleId
        ));

        return ResultContent.buildContent(role.getId());
    }

    @Override
    @Transactional
    public ResultContent<String> updateRole(RoleModel roleModel) {
        return StringUtils.hasText(roleModel.getId()) ? this.modifyRole(roleModel) : this.addRole(roleModel);
    }

    @Override
    @Transactional
    public ResultContent<Void> removeRole(String roleId) {
        Assert.hasText(roleId, "角色id不能为空");

        //查询当前角色所在所有的角色组中
        final List<RoleGroup> roleGroups = this.roleGroupDao.findByRolesIn(Set.of(Role.build(roleId)));

        //查询数据库中的角色
        final Role role = this.roleDao.findTop1ById(roleId);
        if (role == null) {
            return ResultContent.build(ResultState.RoleNotExists);
        }

        //删除角色表中的角色
        boolean success = this.roleDao.removeById(roleId) > 0;
        if (success) {
            roleGroups.forEach((roleGroup) -> {
                this.roleGroupDao.removeRole(roleGroup, Set.of(role));
            });
            this.updateEnterpriseUserFromRoleGroup(roleGroups.stream().map(it -> it.getId()).collect(Collectors.toSet()));
            this.authEventStreamHelper.publish(new RoleStreamModel(
                    AuthEventType.Remove,
                    roleId
            ));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<RoleModel> getRole(String roleId) {
        Assert.hasText(roleId, "角色id不能为空");
        return ResultContent.buildContent(toRoleModel(this.roleDao.findTop1ById(roleId)));
    }


    // ---------------- 角色组 ------------

    private ResultContent<String> addRoleGroup(RoleGroupModel roleGroupModel) {
        //新增校验
        if (!StringUtils.hasText(roleGroupModel.getEnterpriseId())) {
            return ResultContent.build(ResultState.EnterpriseNotNull);
        }
        if (!this.enterpriseDao.existsById(roleGroupModel.getEnterpriseId())) {
            return ResultContent.build(ResultState.EnterpriseNotExist);
        }

        if (!StringUtils.hasText(roleGroupModel.getName())) {
            return ResultContent.build(ResultState.RoleGroupNameNotNull);
        }
        if (this.roleGroupDao.existsByEnterpriseAndName(Enterprise.build(roleGroupModel.getEnterpriseId()), roleGroupModel.getName())) {
            return ResultContent.build(ResultState.RoleGroupExists);
        }

        //新增需要设置企业,创建角色必须包含企业
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setEnterprise(Enterprise.build(roleGroupModel.getEnterpriseId()));
        return this.saveRoleGroup(roleGroupModel, roleGroup);
    }

    private ResultContent<String> modifyRoleGroup(RoleGroupModel roleGroupModel) {
        RoleGroup roleGroup = this.roleGroupDao.findTop1ById(roleGroupModel.getId());
        if (roleGroup == null) {
            return ResultContent.build(ResultState.RoleGroupNotExists);
        }
        //目标角色名存在
        if (this.roleGroupDao.existsByEnterpriseAndName(roleGroup.getEnterprise(), roleGroupModel.getName())) {
            return ResultContent.build(ResultState.RoleGroupExists);
        }

        if (StringUtils.hasText(roleGroupModel.getEnterpriseId()) && !roleGroup.getEnterprise().getId().equals(roleGroupModel.getEnterpriseId())) {
            return ResultContent.build(ResultState.RoleGroupEnterpriseDoNotUpdate);
        }
        return this.saveRoleGroup(roleGroupModel, roleGroup);
    }

    private ResultContent<String> saveRoleGroup(RoleGroupModel roleGroupModel, RoleGroup roleGroup) {
        //忽略要过滤的字段
        Set<String> ignoreProperties = new HashSet<>() {{
            add("id");
        }};
        //过滤拷贝为null的字段
        BeanUtil.getNullPropertyNames(roleGroupModel, ignoreProperties);
        //拷贝为数据库实体
        BeanUtils.copyProperties(roleGroupModel, roleGroup, ignoreProperties.toArray(new String[0]));


        //角色组中的角色
        Optional.ofNullable(roleGroupModel.getRoleId()).ifPresent((roleIds) -> {
            //仅保存有的角色，无效或者数据库中查不到角色不会被保存
            roleGroup.setRoles(roleDao.findByIdIn(roleIds));
        });


        this.dbHelper.saveTime(roleGroup);
        this.roleGroupDao.save(roleGroup);

        //取出角色id
        final String roleGroupId = roleGroup.getId();
        //更新企业角色中的用户
        if (StringUtils.hasText(roleGroupModel.getId())) {
            this.updateEnterpriseUserFromRoleGroup(Set.of(roleGroupId));
        }

        //发布事件
        this.authEventStreamHelper.publish(new RoleGroupStreamModel(
                StringUtils.hasText(roleGroupModel.getId()) ? AuthEventType.Modify : AuthEventType.Add,
                roleGroupId
        ));

        return ResultContent.buildContent(roleGroup.getId());
    }

    @Override
    @Transactional
    public ResultContent<String> updateRoleGroup(RoleGroupModel roleGroupModel) {
        return StringUtils.hasText(roleGroupModel.getId()) ? this.modifyRoleGroup(roleGroupModel) : this.addRoleGroup(roleGroupModel);
    }

    @Override
    @Transactional
    public ResultContent<Void> removeRoleGroup(String roleGroupId) {
        //查询角色组
        final RoleGroup roleGroup = this.roleGroupDao.findTop1ById(roleGroupId);
        if (roleGroup == null) {
            return ResultContent.build(ResultState.RoleGroupNotExists);
        }

        //企业id
        final String enterpriseId = roleGroup.getEnterprise().getId();
        //角色组中的用户
        final Set<String> users = this.roleGroupUserDao.findByRoleGroup(roleGroup).stream().map((it) -> it.getUser().getId()).collect(Collectors.toSet());

        boolean success = this.roleGroupDao.removeById(roleGroupId) > 0;
        //删除角色组的用户
        this.roleGroupUserDao.removeByRoleGroup(roleGroup);
        if (success) {
            //更新企业中的用户
            this.updateEnterpriseUserFromUid(enterpriseId, users);

            this.authEventStreamHelper.publish(new RoleGroupStreamModel(
                    AuthEventType.Remove,
                    roleGroupId
            ));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<RoleGroupModel> getRoleGroup(String roleGroupId) {
        return ResultContent.buildContent(toRoleGroupModel(this.roleGroupDao.findTop1ById(roleGroupId)));
    }

    @Override
    public ResultContent<List<RoleGroupModel>> listRoleGroupFromEnterprise(String enterpriseId) {
        return ResultContent.buildContent(
                this.roleGroupDao
                        .findByEnterprise(Enterprise.build(enterpriseId))
                        .stream()
                        .map(it -> toRoleGroupModel(it))
                        .collect(Collectors.toSet()));
    }


    @Override
    public ResultContent<List<RoleGroupModel>> findByIdentity(String enterpriseId, String[] identity) {
        return ResultContent.buildContent(
                this.roleGroupDao
                        .findByEnterpriseAndIdentityIn(Enterprise.build(enterpriseId), Set.of(identity))
                        .stream()
                        .map(it -> toRoleGroupModel(it))
                        .collect(Collectors.toSet())
        );
    }


    // -------------- 角色组与角色关系--------------------
    @Override
    @Transactional
    public ResultContent<Void> addRoleToRoleGroup(String roleGroupId, String[] roleId) {
        RoleGroup roleGroup = this.roleGroupDao.findTop1ById(roleGroupId);
        if (roleGroup == null) {
            return ResultContent.build(ResultState.RoleGroupNotExists);
        }
        boolean success = this.roleGroupDao.addRole(roleGroup, Arrays.stream(roleId).map(it -> Role.build(it)).collect(Collectors.toSet()));
        if (success) {
            //更新角色组
            this.updateEnterpriseUserFromRoleGroup(Set.of(roleGroupId));
            this.authEventStreamHelper.publish(new RoleGroupStreamModel(
                    AuthEventType.Modify,
                    roleGroupId
            ));
        }
        return ResultContent.build(success);
    }

    @Override
    @Transactional
    public ResultContent<Void> removeRoleFromRoleGroup(String roleGroupId, String[] roleId) {
        RoleGroup roleGroup = this.roleGroupDao.findTop1ById(roleGroupId);
        if (roleGroup == null) {
            return ResultContent.build(ResultState.RoleGroupNotExists);
        }
        boolean success = this.roleGroupDao.removeRole(roleGroup, Arrays.stream(roleId).map(it -> Role.build(it)).collect(Collectors.toSet()));
        if (success) {
            //更新角色组
            this.updateEnterpriseUserFromRoleGroup(Set.of(roleGroupId));
            this.authEventStreamHelper.publish(new RoleGroupStreamModel(
                    AuthEventType.Modify,
                    roleGroupId
            ));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<List<RoleGroupModel>> listRoleGroupFromRole(String[] roleId) {
        return ResultContent.buildContent(
                this.roleGroupDao
                        .findByRolesIn(Stream.of(roleId).map(it -> Role.build(it)).collect(Collectors.toSet()))
                        .stream()
                        .map(it -> toRoleGroupModel(it)).collect(Collectors.toList())
        );
    }

    //----角色组与用户

    @Override
    @Transactional
    public ResultContent<Set<String>> addUserToRoleGroup(String roleGroupId, String[] uid) {
        final RoleGroup roleGroup = this.roleGroupDao.findTop1ById(roleGroupId);
        if (roleGroup == null) {
            return ResultContent.build(ResultState.RoleGroupNotExists);
        }
        if (uid == null || uid.length == 0) {
            return ResultContent.build(ResultState.UserNotNull);
        }
        Set<String> successUser = new HashSet<>();
        Arrays.stream(uid).forEach((u) -> {
            if (!this.roleGroupUserDao.existsByRoleGroupAndUser(roleGroup, User.build(u)) && this.userDao.existsById(u)) {
                RoleGroupUser roleGroupUser = new RoleGroupUser();
                roleGroupUser.setEnterprise(roleGroup.getEnterprise());
                roleGroupUser.setRoleGroup(roleGroup);
                roleGroupUser.setUser(User.build(u));
                this.roleGroupUserDao.save(roleGroupUser);
                successUser.add(u);
            }
        });

        if (successUser.size() > 0) {
            this.updateEnterpriseUserFromUid(roleGroup.getEnterprise().getId(),successUser);
            this.authEventStreamHelper.publish(new RoleGroupUserStreamModel(
                    AuthEventType.Add,
                    roleGroupId,
                    successUser.toArray(new String[0])
            ));
        }

        return ResultContent.buildContent(successUser);
    }

    @Override
    @Transactional
    public ResultContent<Set<String>> addUsersToRoleGroup(String uid, String[] roleGroupIds) {
        if (!this.userDao.existsById(uid)) {
            return ResultContent.build(ResultState.UserNotExists);
        }
        List<RoleGroup> roles = roleGroupDao.findByIdIn(Arrays.stream(roleGroupIds).collect(Collectors.toSet()));
        Map<String, RoleGroup> roleGroupMap = roles.stream().collect(Collectors.toMap(RoleGroup::getId, Function.identity()));
        Set<RoleGroupUser> roleGroupUsers = Arrays
                .stream(roleGroupIds)
                .filter(it -> roleGroupMap.containsKey(it))
                .map(it -> {
                    RoleGroupUser roleGroupUser = new RoleGroupUser();
                    RoleGroup roleGroup = roleGroupMap.get(it);
                    roleGroupUser.setEnterprise(roleGroup.getEnterprise());
                    roleGroupUser.setRoleGroup(roleGroup);
                    roleGroupUser.setUser(User.build(uid));
                    return roleGroupUser;
                }).filter((it) -> {
                    return !this.roleGroupUserDao.existsByRoleGroupAndUser(it.getRoleGroup(), it.getUser());
                }).collect(Collectors.toSet());
        List<RoleGroupUser> success = this.roleGroupUserDao.saveAll(roleGroupUsers);
        if (success.size() > 0) {
            Map<Enterprise, List<RoleGroupUser>> group = success.stream().collect(Collectors.groupingBy(RoleGroupUser::getEnterprise));
            group.forEach((key, value) -> {
                this.updateEnterpriseUserFromUid(key.getId(), value.stream().map(it -> it.getUser().getId()).collect(Collectors.toList()));
            });
            success.forEach(it -> {
                this.authEventStreamHelper.publish(new RoleGroupUserStreamModel(
                        AuthEventType.Add,
                        it.getId(),
                        new String[]{it.getUser().getId()}));
            });
        }
        return ResultContent.build(success.size() > 0 ? ResultState.Success : ResultState.Fail,
                success.stream().map(it -> it.getRoleGroup().getId()).collect(Collectors.toSet())
        );
    }

    @Override
    @Transactional
    public ResultContent<Set<String>> removeUserFromRoleGroup(String roleGroupId, String[] uid) {
        final RoleGroup roleGroup = this.roleGroupDao.findTop1ById(roleGroupId);
        if (roleGroup == null) {
            return ResultContent.build(ResultState.RoleGroupNotExists);
        }
        if (uid == null || uid.length == 0) {
            return ResultContent.build(ResultState.UserNotNull);
        }
        Set<String> successUser = new HashSet<>();
        Arrays.stream(uid).forEach((u) -> {
            if (this.roleGroupUserDao.removeByRoleGroupAndUser(roleGroup, User.build(u)) > 0) {
                successUser.add(u);
            }
        });

        if (successUser.size() > 0) {
            this.updateEnterpriseUserFromUid(roleGroup.getEnterprise().getId(), successUser);
            this.authEventStreamHelper.publish(new RoleGroupUserStreamModel(
                    AuthEventType.Remove,
                    roleGroupId,
                    successUser.toArray(new String[0])
            ));
        }

        return ResultContent.buildContent(successUser);
    }

    @Override
    public ResultContent<Page<String>> listUserFromRoleGroup(String roleGroupId, Pageable pageable) {
        return ResultContent.buildContent(
                PageEntityUtil.toPageModel(this.roleGroupUserDao.findByRoleGroup(RoleGroup.build(roleGroupId), pageable), (it) -> {
                    return it.getUser().getId();
                }));
    }

    @Override
    public ResultContent<List<RoleGroupModel>> listRoleGroupFromEnterpriseUser(String enterpriseId, String uid) {
        List<RoleGroupUser> list = this.roleGroupUserDao.findByEnterpriseAndUserIn(Enterprise.build(enterpriseId), User.build(uid));
        if (CollectionUtils.isEmpty(list)){
            return ResultContent.build(ResultState.Fail);
        }
        return ResultContent.buildContent(list
                        .stream()
                        .map(it -> toRoleGroupModel(it.getRoleGroup()))
                        .collect(Collectors.toList())
        );
    }


    private RoleGroupUserModel toRoleGroupUserModel(RoleGroupUser roleGroupUser) {
        RoleGroupUserModel roleGroupUserModel = new RoleGroupUserModel();

        Optional.ofNullable(roleGroupUser.getRoleGroup()).ifPresent((roleGroup) -> {
            roleGroupUserModel.setRoleGroup(roleGroup.getId());
        });

        Optional.ofNullable(roleGroupUser.getEnterprise()).ifPresent((enterprise) -> {
            roleGroupUserModel.setEnterprise(enterprise.getId());
        });

        Optional.ofNullable(roleGroupUser.getUser()).ifPresent((user) -> {
            roleGroupUserModel.setUser(user.getId());
        });

        return roleGroupUserModel;
    }


    private RoleGroupModel toRoleGroupModel(RoleGroup roleGroup) {
        if (roleGroup == null) {
            return null;
        }
        RoleGroupModel roleGroupModel = new RoleGroupModel();
        BeanUtils.copyProperties(roleGroup, roleGroupModel, "roles", "enterprise");

        //角色
        Optional.ofNullable(roleGroup.getRoles()).ifPresent((roles) -> {
            roleGroupModel.setRoleId(roles.stream().map(it -> it.getId()).collect(Collectors.toSet()));
        });


        //企业
        Optional.ofNullable(roleGroup.getEnterprise()).ifPresent((enterprise) -> {
            roleGroupModel.setEnterpriseId(enterprise.getId());
        });


        return roleGroupModel;
    }


    private RoleModel toRoleModel(Role role) {
        if (role == null) {
            return null;
        }
        RoleModel roleModel = new RoleModel();
        BeanUtils.copyProperties(role, roleModel);

        //企业
        Optional.ofNullable(role.getEnterprise()).ifPresent((enterprise) -> {
            roleModel.setEnterpriseId(enterprise.getId());
        });

        return roleModel;
    }


    private void updateEnterpriseUserFromRole(Collection<String> roleId) {
        this.updateEnterpriseUserFromRoleGroup(
                this.roleGroupDao.findByRolesIn(roleId.stream().map(it -> Role.build(it)).collect(Collectors.toSet()))
                        .stream().map(it -> it.getId()).collect(Collectors.toSet())
        );
    }


    private void updateEnterpriseUserFromRoleGroup(Collection<String> roleGroupId) {
        //查询所有的角色组与用户的关系
        List<RoleGroupUser> roleGroupUsers = this.roleGroupUserDao.findByRoleGroupIn(roleGroupId.stream().map(it -> RoleGroup.build(it)).collect(Collectors.toSet()));
        Map<Enterprise, List<RoleGroupUser>> ret = roleGroupUsers.stream().collect(Collectors.groupingBy(RoleGroupUser::getEnterprise, Collectors.toList()));
        ret.entrySet().forEach((entry) -> {
            this.updateEnterpriseUserFromUid(entry.getKey().getId(), entry.getValue().stream().map(it -> it.getUser().getId()).collect(Collectors.toSet()));
        });
    }


    private void updateEnterpriseUserFromUid(final String enterpriseId, final Collection<String> uid) {
        if (!StringUtils.hasText(enterpriseId)) {
            return;
        }
        if (uid == null || uid.size() == 0) {
            return;
        }
        //同步用户信息
        this.enterpriseUserService.update(enterpriseId, uid.toArray(new String[0]));
    }


}
