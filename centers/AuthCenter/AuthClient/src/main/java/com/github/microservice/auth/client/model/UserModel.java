package com.github.microservice.auth.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    //用户id
    private String userId;

    //账号
    private String userName;

    //手机号码
    private String phone;

    //邮箱
    private String email;

    //身份证
    private String idcard;


}
