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

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "elasticsearch.querybuild")
public class EsQueryBuildConfig {
    /**
     * queries.
     */
    private List<BuildQuery> queries = new ArrayList<>();

    /**
     * BuildQuery.
     */
    @Getter
    @Setter
    public static class BuildQuery {
        /**
         * 数据源.
         */
        private String source;
        /**
         * match Phrase query list.
         */
        private List<MatchQuery> matchPhraseQueries;
        /**
         * match  query list.
         */
        private List<MatchQuery> matchQueries;
        /**
         * founction score list.
         */
        private List<FounctionScore> functions;
        /**
         * highlight.
         */
        private Highlight highlight;
    }

    /**
     * MatchQuery.
     */
    @Getter
    @Setter
    public static class MatchQuery {
        /**
         * match 字段名称.
         */
        private String name;
        /**
         * 分词器.
         */
        private String analyzer;
        /**
         * slop.
         */
        private Integer slop;
        /**
         * boost.
         */
        private Float boost;
    }

    /**
     * FounctionScore.
     */
    @Getter
    @Setter
    public static class FounctionScore {
        /**
         * term key.
         */
        private String termkey;
        /**
         * key value.
         */
        private String keyValue;
        /**
         * 权重.
         */
        private Float weight;
    }

    /**
     * Highlight.
     */
    @Getter
    @Setter
    public static class Highlight {
        /**
         * 需要高亮的字段名称.
         */
        private List<String> fields;
        /**
         * 高亮fragment Size.
         */
        private Integer fragmentSize;
        /**
         * 高亮部分标签前缀.
         */
        private String preTags;
        /**
         * 高亮部分标签后缀.
         */
        private String postTags;
    }


}
