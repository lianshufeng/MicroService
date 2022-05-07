package com.github.microservice.auth.server.core.domain;


import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "enterprise_name_unique", def = "{enterprise:1, name:1}", unique = true)
})
public class RoleGroup extends SuperEntity {


    /**
     * 关联的企业
     */
    @Indexed
    @DBRef(lazy = true)
    private Enterprise enterprise;


    //角色组的名称
    @Indexed
    private String name;

    //角色组的备注
    private String remark;

    //身份标识
    @Indexed
    private Set<String> identity;


    @Indexed
    @DBRef(lazy = true)
    private Set<Role> roles;


    /**
     * 构建角色组
     *
     * @param id
     * @return
     */
    public static RoleGroup build(String id) {
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setId(id);
        return roleGroup;
    }


}
