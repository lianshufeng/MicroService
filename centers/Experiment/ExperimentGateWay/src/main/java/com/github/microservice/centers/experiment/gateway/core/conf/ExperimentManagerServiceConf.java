package com.github.microservice.centers.experiment.gateway.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "experiment.manager")
public class ExperimentManagerServiceConf {

    private String serviceName = "ExperimentManager";

}
