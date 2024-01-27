package com.search.docsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class EsfunctionScoreConfig {
    public List<Map> functionscore;
    public Float titleBoost;
    public Float textContentBoost;
    public String limitType;
    public static String esExistingKey;
}
