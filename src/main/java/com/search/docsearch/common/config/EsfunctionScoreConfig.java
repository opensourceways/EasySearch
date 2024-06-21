package com.search.docsearch.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "elasticsearch")
@Data
public class EsfunctionScoreConfig {
    /**
     * the boost of functions.
     */
    public List<Map> functionscore;
    /**
     * the boost socre of tittle.
     */
    public Float titleBoost;
    /**
     * the boost of text.
     */
    public Float textContentBoost;
    /**
     * h1 boost score.
     */
    public Float h1Boost;
    /**
     * h2 boost score.
     */
    public Float h2Boost;
    /**
     * h3 boost score.
     */
    public Float h3Boost;
    /**
     * h4 boost score.
     */
    public Float h4Boost;
    /**
     * h5 boost score.
     */
    public Float h5Boost;
    /**
     * Maximum Boost score .
     */
    public Float strongBoost;
    /**
     * the limit search type.
     */
    public String limitType;

    /**
     * the exsit key word.
     */
    public String esExistingKey;
}
