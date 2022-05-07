package com.github.microservice.auth.server.core.domain;

import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import net.bytebuddy.asm.Advice;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Enterprise extends SuperEntity {

    /**
     * 企业名称，不能重复
     */
    @Indexed
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
    public static Enterprise build(String id) {
        Enterprise enterprise = new Enterprise();
        enterprise.setId(id);
        return enterprise;
    }


}
