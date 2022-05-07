package com.github.microservice.auth.server.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * 用户与企业之前的关系,一般用于缓存
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "user_enterprise_unique", def = "{user:1, enterprise:1}", unique = true)
})
public class EnterpriseUser extends SuperEntity {

    @DBRef(lazy = true)
    private Enterprise enterprise;


    @DBRef(lazy = true)
    private User user;

    //权限
    @Indexed
    private Set<String> auth;

    //身份
    @Indexed
    private Set<String> identity;

    //角色
    @Indexed
    private Set<String> roleId;

    //角色组
    @Indexed
    private Set<String> roleGroupId;


}
