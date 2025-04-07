package com.rkm.projectmanagement.system.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "api")
public class ApplicationProperties {
    private String restClientBaseurl;
}
