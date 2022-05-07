package com.github.microservice.helper.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBuildTask {

    /**
     * 创建时间
     */
    private long createTime;


}
