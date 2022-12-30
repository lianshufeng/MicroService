package com.github.microservice.components.push.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeTuiMessage extends BaseMessage{

    private String uid;
}
