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
public class Role extends SuperEntity {

    //企业
    @Indexed
    @DBRef(lazy = true)
    private Enterprise enterprise;

    //角色名
    @Indexed(sparse = false)
    private String name;

    //权限
    @Indexed
    private Set<String> auth;


    /**
     * 备注信息
     */
    private String remark;


    /**
     * 构建角色
     *
     * @param id
     * @return
     */
    public static Role build(String id) {
        Role role = new Role();
        role.setId(id);
        return role;
    }

}
