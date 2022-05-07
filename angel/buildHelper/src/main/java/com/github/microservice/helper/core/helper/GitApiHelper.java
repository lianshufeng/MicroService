package com.github.microservice.helper.core.helper;

import com.github.microservice.helper.core.model.GitInfo;
import com.github.microservice.helper.core.type.GitType;

import java.util.Map;

public abstract class GitApiHelper {


    /**
     * 获取用户所有的仓库
     *
     * @return
     */
    public abstract Map<String, GitInfo> userRepos();


    /**
     * git 的类型
     *
     * @return
     */
    public abstract GitType gitType();

}
