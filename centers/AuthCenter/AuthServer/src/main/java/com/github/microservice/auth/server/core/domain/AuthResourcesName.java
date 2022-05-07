package com.github.microservice.auth.server.core.domain;

import com.github.microservice.auth.client.type.ResourceType;
import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Document
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "resourceType_authType_name_unique", def = "{ resourceType:1,authType:1,name:1}", unique = true)
})
public class AuthResourcesName extends SuperEntity {


    //资源类型
    @Indexed
    private ResourceType resourceType;

    //权限类型
    @Indexed
    private AuthType authType;

    //资源名
    @Indexed
    private String name;


    //备注
    private String remark;


    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date TTL;

}
