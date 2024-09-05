package com.search.common.config.es;

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
    public Float h1Boost;
    public Float h2Boost;
    public Float h3Boost;
    public Float h4Boost;
    public Float h5Boost;
    public Float strongBoost;

    public String limitType;
    public  String esExistingKey;
}
