package com.github.microservice.auth.server.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TimeSeries(collection="weather", timeField = "timestamp")
public class TestTimer {

    private String id;

    private String name;

    private long timestamp;


}
