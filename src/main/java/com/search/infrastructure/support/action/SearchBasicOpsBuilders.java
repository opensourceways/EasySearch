package com.search.infrastructure.support.action;

import com.alibaba.fastjson.JSONObject;
import com.search.common.util.ObjectMapperUtil;
import com.search.infrastructure.support.config.EsQueryBuildConfig;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Map;


public class SearchBasicOpsBuilders {


    public static SearchRequest getDefaultSearchRequest(String index) {
        SearchRequest request = new SearchRequest(index);
        return request;
    }


    public static BoolQueryBuilder buildBoolQuery() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        return boolQueryBuilder;
    }


    public static void buildShouldQuery(BoolQueryBuilder boolQueryBuilder, String keyWord, List<EsQueryBuildConfig.MatchQuery> matchQueries, List<EsQueryBuildConfig.MatchQuery> matchPhraseQueries) {
        if (!CollectionUtils.isEmpty(matchQueries)) {
            buildMatchQueries(matchQueries, boolQueryBuilder, keyWord);
        }
        if (!CollectionUtils.isEmpty(matchPhraseQueries)) {
            buildMatchPhraseQueriesr(matchPhraseQueries, boolQueryBuilder, keyWord);
        }
        boolQueryBuilder.minimumShouldMatch(1);
    }


    public static void buildMustNotQuery(BoolQueryBuilder boolQueryBuilder, List limit, List filter) {
        if (!CollectionUtils.isEmpty(limit)) {
            limit.stream().forEach(a -> {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                Map<String, Object> map = ObjectMapperUtil.toMap(JSONObject.toJSONString(a));
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = String.valueOf(entry.getValue());
                    vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                }
                boolQueryBuilder.mustNot(vBuilder);

            });
        }
        if (!CollectionUtils.isEmpty(filter)) {
            BoolQueryBuilder zBuilder = QueryBuilders.boolQuery();
            filter.stream().forEach(a -> {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                Map<String, Object> map = ObjectMapperUtil.toMap(JSONObject.toJSONString(a));
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = String.valueOf(entry.getValue());
                    vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                }
                zBuilder.should(vBuilder);
            });

            boolQueryBuilder.filter(zBuilder);
        }

    }


    public static HighlightBuilder buildHighlightBuilder(EsQueryBuildConfig.Highlight highlight) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlight.getFields().forEach(a -> highlightBuilder.field(a));
        highlightBuilder.fragmentSize(highlight.getFragmentSize());
        highlightBuilder.preTags(highlight.getPreTags());
        highlightBuilder.postTags(highlight.getPostTags());
        return highlightBuilder;
    }


    public static FunctionScoreQueryBuilder buildFunctionScoreQuery(BoolQueryBuilder boolQueryBuilder, List<EsQueryBuildConfig.FounctionScore> functions) {
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionBuilder = new FunctionScoreQueryBuilder.FilterFunctionBuilder[functions.size()];
        for (int i = 0; i < functions.size(); i++) {
            EsQueryBuildConfig.FounctionScore founctionScore = functions.get(i);
            functionBuilder[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery(founctionScore.getTermkey(), founctionScore.getKeyValue()), ScoreFunctionBuilders.weightFactorFunction(founctionScore.getWeight()));
        }
        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQueryBuilder, functionBuilder)
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                .boostMode(CombineFunction.MULTIPLY);
        return functionScoreQuery;
    }


    public static void buildMatchQueries(List<EsQueryBuildConfig.MatchQuery> matchQueriesConfig, BoolQueryBuilder boolQueryBuilder, String keyWord) {
        for (EsQueryBuildConfig.MatchQuery matchQueryConfig : matchQueriesConfig) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(matchQueryConfig.getName(), keyWord).analyzer(matchQueryConfig.getAnalyzer());
            if (matchQueryConfig.getBoost() != null && matchQueryConfig.getBoost() > 0) {
                matchQueryBuilder.boost(matchQueryConfig.getBoost());
            }
            boolQueryBuilder.should(matchQueryBuilder);
        }
    }

    public static void buildMatchPhraseQueriesr(List<EsQueryBuildConfig.MatchQuery> matchQueriesConfig, BoolQueryBuilder boolQueryBuilder, String keyWord) {
        for (EsQueryBuildConfig.MatchQuery matchQueryConfig : matchQueriesConfig) {
            MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(matchQueryConfig.getName(), keyWord).analyzer(matchQueryConfig.getAnalyzer()).boost(matchQueryConfig.getBoost() == null ? 1 : matchQueryConfig.getBoost());
            if (matchQueryConfig.getSlop() != null) {
                matchPhraseQueryBuilder.slop(matchQueryConfig.getSlop());
            }
            boolQueryBuilder.should(matchPhraseQueryBuilder);
        }
    }
}
