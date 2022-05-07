package com.github.microservice.discovery.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceHealth {
    private String Node;
    private String CheckID;
    private String Status;
    private String Output;
    private String ServiceID;
    private String ServiceName;
}
