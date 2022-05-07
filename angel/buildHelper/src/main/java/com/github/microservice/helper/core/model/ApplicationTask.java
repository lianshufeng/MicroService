package com.github.microservice.helper.core.model;

import com.github.microservice.helper.core.model.ApplicationGitInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationTask {

    /**
     * 应用配置
     */
    private Map<String, ApplicationGitInfo> applications;


}
