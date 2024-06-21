package com.search.docsearch.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "software")
@Configuration
public class SoftwareSearchConfig {
    /**
     * index of software.
     */
    public String index;
}
