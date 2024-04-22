package com.github.microservice.auth.server.core.domain;

import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends SuperEntity {

    /**
     * 企业名称，不能重复
     */
    @Indexed(unique = true)
    private String name;


    /**
     * 禁用
     */
    @Indexed
    private boolean disable;


    /**
     * 企业类型
     */
    @Indexed
    private AuthType authType = AuthType.Enterprise;


    /**
     * 备注
     */
    private String remark;


    /**
     * 构建企业
     *
     * @param id
     * @return
     */
    public static Organization build(String id) {
        Organization org = new Organization();
        org.setId(id);
        return org;
    }


}
