package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.model.stream.SuperStreamModel;
import com.github.microservice.auth.client.type.AuthStreamType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamModel {
    private AuthStreamType type;
    private SuperStreamModel parameter;
}
