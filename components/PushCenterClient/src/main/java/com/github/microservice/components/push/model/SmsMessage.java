package com.github.microservice.components.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessage extends BaseMessage{

    /**
     * 平台号码
     */
    private String[] number;


    /**
     * 模版id
     */
    private String templateId;

}
