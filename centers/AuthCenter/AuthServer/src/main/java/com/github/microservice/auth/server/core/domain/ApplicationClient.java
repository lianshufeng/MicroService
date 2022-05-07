package com.github.microservice.auth.server.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@EqualsAndHashCode
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationClient extends SuperEntity {

    //客户端id
    @Indexed(unique = true)
    private String clientId;

    //密钥
    private String secret;

    //权限类型
    private Set<String> authorizedGrantTypes;

    //是否禁止
    @Indexed
    private boolean disable;


}
