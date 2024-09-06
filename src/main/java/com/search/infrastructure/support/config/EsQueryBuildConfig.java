/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.infrastructure.support.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@Data
@ConfigurationProperties(prefix = "elasticsearch.querybuild")
public class EsQueryBuildConfig {
    /**
     * queries
     */
    List<BuildQuery> queries = new ArrayList<>();

    /**
     * BuildQuery
     */
    @Getter
    @Setter
    @Data
    public class BuildQuery {
        String source;
        List<MatchQuery> matchPhraseQueries;
        List<MatchQuery> matchQueries;

        List<FounctionScore> functions;
        Highlight highlight;
    }

    /**
     * MatchQuery
     */
    @Getter
    @Setter
    @Data
    public class MatchQuery {
        String name;
        String analyzer;
        Integer slop;
        Float boost;
    }

    /**
     * FounctionScore
     */
    @Getter
    @Setter
    @Data
    public class FounctionScore {
        String termkey;
        String keyValue;
        Float weight;
    }

    /**
     * Highlight
     */
    @Getter
    @Setter
    @Data
    public class Highlight {
        List<String> fields;
        Integer fragmentSize;
        String preTags;
        String postTags;
    }


}
