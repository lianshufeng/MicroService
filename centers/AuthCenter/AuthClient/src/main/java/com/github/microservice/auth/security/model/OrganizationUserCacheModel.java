package com.github.microservice.auth.security.model;

import com.github.microservice.auth.client.model.OrganizationUserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUserCacheModel extends OrganizationUserModel {


    //其他参数
    private Map<String, Object> other = new ConcurrentHashMap<>();

    //读写other
    public Map<String, Object> other() {
        return this.other;
    }


}
