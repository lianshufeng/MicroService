package com.github.microservice.core.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MVCRequestConfiguration.class, MVCResponseConfiguration.class})
public class MVCConfiguration {


}
