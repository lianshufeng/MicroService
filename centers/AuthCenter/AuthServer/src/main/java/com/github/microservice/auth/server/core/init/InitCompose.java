package com.github.microservice.auth.server.core.init;

import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.model.*;
import com.github.microservice.auth.client.service.RoleService;
import com.github.microservice.auth.client.service.UserService;
import com.github.microservice.auth.server.core.conf.InitConf;
import com.github.microservice.auth.server.core.service.ApplicationClientService;
import com.github.microservice.auth.server.core.service.local.OrganizationServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 初始化权限中心
 */
@Slf4j
@Component
public class InitCompose implements ApplicationRunner {

    @Autowired
    private InitCompose me;

    @Autowired
    private InitConf initConf;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationClientService applicationClientService;

    @Autowired
    private OrganizationServiceImpl organizationService;

    @Autowired
    private RoleService roleService;


    /**
     * 机构初始化入口
     */
    @SneakyThrows
    @Transactional
    public void makeOrganization() {
        //初始化机构
        if (initConf.getOrganizations() == null) {
            return;
        }
        for (InitConf.Organization org : initConf.getOrganizations()) {
            //机构名不能重复
            OrganizationModel organizationModel = this.organizationService.findByName(org.getName()).getContent();
            if (organizationModel == null) {
                organizationModel = new OrganizationModel();
                BeanUtils.copyProperties(org, organizationModel, "roleGroup");
                String oid = this.organizationService.add(organizationModel).getContent();
                organizationModel.setId(oid);
            }
            //注册机构
            this.initRoleGroup(organizationModel.getId(), org.getRoleGroup());
        }
    }


    //初始化
    private void initRoleGroup(String oid, InitConf.RoleGroup[] roleGroup) {
        if (roleGroup == null) {
            return;
        }

        for (InitConf.RoleGroup group : roleGroup) {


            //角色组
            String roleGroupId = null;
            RoleGroupModel roleGroupModel = this.roleService.getRoleGroupByName(group.getName(), oid).getContent();
            if (roleGroupModel == null) {
                roleGroupModel = new RoleGroupModel();
                BeanUtils.copyProperties(group, roleGroupModel, "roleGroup");
                roleGroupModel.setOrganizationId(oid);
                roleGroupId = this.roleService.updateRoleGroup(roleGroupModel).getContent();
            } else {
                roleGroupId = roleGroupModel.getId();
            }


            //创建用户
            final String[] uids = Arrays.stream(group.getUser())
                    //用户不存在才创建
                    .map((user) -> {
                        String uid = null;
                        UserModel userModel = this.userService.queryFromLoginType(user.getLoginType(), user.getLoginValue()).getContent();
                        if (userModel == null) {
                            UserAuthModel userAuthModel = new UserAuthModel();
                            BeanUtils.copyProperties(user, userAuthModel);
                            uid = userService.add(userAuthModel).getContent();
                        } else {
                            uid = userModel.getUserId();
                        }
                        return uid;
                    }).filter(uid -> StringUtils.hasText(uid)).toArray(String[]::new);

            //创建角色
            final String[] roleIds = Arrays.stream(group.getRole())
                    .map(role -> {
                        String roleId = null;
                        RoleModel roleModel = this.roleService.getRoleByName(role.getName(), oid).getContent();
                        if (roleModel == null) {
                            roleModel = new RoleModel();
                            BeanUtils.copyProperties(role, roleModel);
                            roleModel.setOrganizationId(oid);
                            roleId = this.roleService.updateRole(roleModel).getContent();
                        } else {
                            roleId = roleModel.getId();
                        }
                        return roleId;
                    }).toArray(String[]::new);

            //添加用户到角色组
            Set<String> needAddUsers = new HashSet<>(Set.of(uids));
            needAddUsers.removeAll(this.roleService.hasUserInRoleGroup(roleGroupId, uids).getContent());
            if (needAddUsers.size() > 0) {
                Optional.ofNullable(this.roleService.addUserToRoleGroup(roleGroupId, needAddUsers.toArray(String[]::new)).getContent()).ifPresent((ret) -> {
                    log.info("addUserToRoleGroup : {}", ret);
                });
            }


            //添加角色到角色组
            Set<String> needAddRoles = new HashSet<>(Set.of(roleIds));
            Optional.ofNullable(roleService.getRoleGroup(roleGroupId).getContent()).ifPresent(groupModel -> {
                if (groupModel.getRoleId() != null) {
                    needAddRoles.removeAll(groupModel.getRoleId());
                }
            });
            if (needAddRoles.size() > 0) {
                Optional.ofNullable(this.roleService.addRoleToRoleGroup(roleGroupId, needAddRoles.toArray(String[]::new)).getContent()).ifPresent((ret) -> {
                    log.info("addRoleToRoleGroup : {}", ret);
                });
            }


        }


    }


    @SneakyThrows
    private void initApplicationClient() {
        Optional.ofNullable(initConf.getClient()).ifPresent((items) -> {
            for (InitConf.ApplicationClient client : items) {
                this.applicationClientService.add(client.getClientId(), client.getSecret(), client.getAuthorizedGrantTypes());
            }
        });

    }

//    @SneakyThrows
//    private void initUser() {
//        Optional.ofNullable(initConf.getUser()).ifPresent((items) -> {
//            for (InitConf.User user : items) {
//                userService.add(UserAuthModel
//                        .builder()
//                        .loginType(user.getLoginType())
//                        .loginValue(user.getLoginValue())
//                        .passWord(user.getPassWord())
//                        .build());
//            }
//        });
//    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initApplicationClient();
        me.makeOrganization();
    }
}
