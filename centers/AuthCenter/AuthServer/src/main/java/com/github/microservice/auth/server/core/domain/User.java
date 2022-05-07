package com.github.microservice.auth.server.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User extends SuperEntity {

    //账号
    @Indexed(unique = true, sparse = true)
    private String userName;

    //手机号码
    @Indexed(unique = true, sparse = true)
    private String phone;

    //邮箱
    @Indexed(unique = true, sparse = true)
    private String email;

    //身份证
    @Indexed(unique = true, sparse = true)
    private String idcard;


    //密码
    private String passWord;


    //是否禁止
    @Indexed
    private boolean disable;

    /**
     * 构建用户
     *
     * @param uid
     * @return
     */
    public static User build(String uid) {
        Assert.hasText(uid, "用户id不能为空");
        User user = new User();
        user.setId(uid);
        return user;
    }


}
