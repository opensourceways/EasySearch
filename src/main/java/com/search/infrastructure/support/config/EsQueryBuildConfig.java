package com.search.infrastructure.support.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "elasticsearch.querybuild")
public class EsQueryBuildConfig {
    List<BuildQuery> queries=new ArrayList<>();

    @Data
    public class BuildQuery {
        String source;
        List<MatchQuery> matchPhraseQueries;
        List<MatchQuery> matchQueries;

        List<FounctionScore> functions;
        Highlight highlight;
    }


    @Data
    public class MatchQuery {
        String name;
        String analyzer;
        Integer slop;
        Float boost;
    }


    @Data
    public class FounctionScore {
        String termkey;
        String keyValue;
        Float weight;
    }

    @Data
    public class Highlight {
        List<String> fields;
        Integer fragmentSize;
        String preTags;
        String postTags;
    }


}
