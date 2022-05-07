package com.github.microservice.auth.server.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "enterprise_roleGroup_user_unique", def = "{enterprise:1, roleGroup:1, user:1}", unique = true)
})
public class RoleGroupUser extends SuperEntity {

    //企业
    @Indexed
    @DBRef(lazy = true)
    private Enterprise enterprise;

    //角色组
    @Indexed
    @DBRef(lazy = true)
    private RoleGroup roleGroup;

    //拥护
    @Indexed
    @DBRef(lazy = true)
    private User user;





    /**
     * 构建对象
     *
     * @param uid
     * @return
     */
    public static RoleGroupUser build(String epId, String roleGroupId, String uid) {
        return build(Enterprise.build(epId), RoleGroup.build(roleGroupId), User.build(uid));
    }

    /**
     * 构建对象
     *
     * @param user
     * @return
     */
    public static RoleGroupUser build(Enterprise enterprise, RoleGroup roleGroup, User user) {
        return RoleGroupUser.builder().enterprise(enterprise).roleGroup(roleGroup).user(user).build();
    }

}
