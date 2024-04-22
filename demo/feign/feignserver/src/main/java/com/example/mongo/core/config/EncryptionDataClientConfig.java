package com.example.mongo.core.config;

import com.github.microservice.encryption.config.EncryptionClientConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(EncryptionClientConfiguration.class)
public class EncryptionDataClientConfig {
}
