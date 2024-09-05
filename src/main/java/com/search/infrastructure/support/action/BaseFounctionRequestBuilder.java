package com.search.infrastructure.support.action;

import com.alibaba.fastjson.JSONObject;
import com.search.common.thread.ThreadLocalCache;
import com.search.common.util.General;
import com.search.common.util.ObjectMapperUtil;
import com.search.domain.base.dto.*;
import com.search.infrastructure.support.config.EsQueryBuildConfig;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component

public class BaseFounctionRequestBuilder {
    @Autowired
    EsQueryBuildConfig esQueryBuildConfig;


    public SearchRequest getDefaultSearchRequest(String index) {
        SearchRequest request = new SearchRequest(index);
        return request;
    }


    public BoolQueryBuilder buildBoolQuery() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        return boolQueryBuilder;
    }


    public void buildShouldQuery(BoolQueryBuilder boolQueryBuilder, String keyWord) {
        List<EsQueryBuildConfig.BuildQuery> queries = esQueryBuildConfig.getQueries();
        if (queries == null)
            queries = new ArrayList<>();
        String dataSource = ThreadLocalCache.getDataSource();
        List<EsQueryBuildConfig.BuildQuery> collect = queries.stream().filter(buildQuery -> dataSource.equals(buildQuery.getSource())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", keyWord).analyzer("ik_max_word").slop(2);
            ptitleMP.boost(200);
            MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", keyWord).analyzer("ik_max_word").slop(2);
            ptextContentMP.boost(100);
            boolQueryBuilder.should(ptitleMP).should(ptextContentMP);
            MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", keyWord).analyzer("ik_smart");
            titleMP.boost(2);
            MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", keyWord).analyzer("ik_smart");
            textContentMP.boost(1);
            boolQueryBuilder.should(titleMP).should(textContentMP);

            boolQueryBuilder.minimumShouldMatch(1);
        } else {
            EsQueryBuildConfig.BuildQuery buildQuery = collect.get(0);
            List<EsQueryBuildConfig.MatchQuery> matchQueries = buildQuery.getMatchQueries();
            if (!CollectionUtils.isEmpty(matchQueries)) {
                buildMatchQueriesByConfig(matchQueries, boolQueryBuilder, keyWord);
            }
            List<EsQueryBuildConfig.MatchQuery> matchPhraseQueries = buildQuery.getMatchPhraseQueries();
            if (!CollectionUtils.isEmpty(matchPhraseQueries)) {
                buildMatchPhraseQueriesrByConfig(matchPhraseQueries, boolQueryBuilder, keyWord);
            }
        }
        boolQueryBuilder.minimumShouldMatch(1);
    }


    public void buildMustNotQuery(BoolQueryBuilder boolQueryBuilder, Object limit, Object filter) {
        if (limit instanceof List) {
            List limitList = (List) limit;
            if (!CollectionUtils.isEmpty(limitList)) {
                limitList.stream().forEach(a -> {
                    BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                    Map<String, Object> map = ObjectMapperUtil.toMap(JSONObject.toJSONString(a));
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = String.valueOf(entry.getValue());
                        if (key.equals("version")) {
                            String[] versions = value.split(",");
                            vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", versions));
                        } else {
                            vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                        }

                    }
                    boolQueryBuilder.mustNot(vBuilder);

                });
            }
        }
        if (filter instanceof List) {
            List lfilterList = (List) filter;
            if (!CollectionUtils.isEmpty(lfilterList)) {
                BoolQueryBuilder zBuilder = QueryBuilders.boolQuery();
                lfilterList.stream().forEach(a -> {
                    BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                    Map<String, Object> map = ObjectMapperUtil.toMap(JSONObject.toJSONString(a));
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = String.valueOf(entry.getValue());
                        if (key.equals("version")) {
                            String[] versions = value.split(",");
                            vBuilder.must(QueryBuilders.termsQuery("version.keyword", versions));
                        } else {
                            vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                        }
                    }
                    zBuilder.should(vBuilder);
                });

                boolQueryBuilder.filter(zBuilder);
            }

        }
    }


    public HighlightBuilder buildHighlightBuilder() {
        List<EsQueryBuildConfig.BuildQuery> queries = esQueryBuildConfig.getQueries();
        if (queries == null)
            queries = new ArrayList<>();
        List<EsQueryBuildConfig.BuildQuery> collect = queries.stream().filter(buildQuery -> ThreadLocalCache.getDataSource().equals(buildQuery.getSource())).collect(Collectors.toList());
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        if (!CollectionUtils.isEmpty(collect) && collect.get(0).getHighlight() != null) {
            EsQueryBuildConfig.Highlight highlight = collect.get(0).getHighlight();
            highlight.getFields().forEach(a -> highlightBuilder.field(a));
            highlightBuilder.fragmentSize(highlight.getFragmentSize());
            highlightBuilder.preTags(highlight.getPreTags());
            highlightBuilder.postTags(highlight.getPostTags());
        } else {
            highlightBuilder.
                    field("textContent")
                    .field("title")
                    .fragmentSize(100)
                    .preTags("<span>")
                    .postTags("</span>");
        }
        return highlightBuilder;
    }


    public FunctionScoreQueryBuilder buildFunctionScoreQuery(BoolQueryBuilder boolQueryBuilder) {
        List<EsQueryBuildConfig.BuildQuery> collect = esQueryBuildConfig.getQueries().stream().filter(buildQuery -> ThreadLocalCache.getDataSource().equals(buildQuery.getSource())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect) && collect.get(0).getFunctions() != null) {
            List<EsQueryBuildConfig.FounctionScore> functions = collect.get(0).getFunctions();

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

        return null;
    }


    public SearchRequest getDefaultDocsSearchRequest(SearchDocsBaseCondition condition) {
        SearchRequest defaultSearchRequest = getDefaultSearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        defaultSearchRequest.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = buildBoolQuery();
        this.buildShouldQuery(boolQueryBuilder, condition.getKeyword());
        this.buildMustNotQuery(boolQueryBuilder, condition.getLimit(), condition.getFilter());
        HighlightBuilder highlightBuilder = buildHighlightBuilder();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = buildFunctionScoreQuery(boolQueryBuilder);
        sourceBuilder.query(functionScoreQueryBuilder == null ? boolQueryBuilder : functionScoreQueryBuilder);
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(condition.getPageFrom()).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        return defaultSearchRequest;
    }


    public SearchRequest getDefaultCountSearchRequest(SearchDocsBaseCondition condition, String field, String terms) {
        SearchRequest defaultSearchRequest = getDefaultSearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        defaultSearchRequest.source(sourceBuilder);
        BoolQueryBuilder boolQueryBuilder = this.buildBoolQuery();
        this.buildShouldQuery(boolQueryBuilder, condition.getKeyword());
        this.buildMustNotQuery(boolQueryBuilder, condition.getLimit(), condition.getFilter());
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.aggregation(AggregationBuilders.terms(terms).field(field));
        return defaultSearchRequest;
    }


    public SearchRequest getDefaultSortSearchRequest(SearchSortBaseCondition condition, Boolean isNeedHighlight) {

        SearchRequest request = new SearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String keyword = condition.getKeyword();
        Map<String, Object> search = ObjectMapperUtil.toMap(JSONObject.toJSONString(condition));
        for (Map.Entry<String, Object> entry : search.entrySet()) {
            String key = entry.getKey();
            if ("keyword".equals(key) || "index".equals(key) || "pageFrom".equals(key) || "page".equals(key) || "pageSize".equals(key)) {
                continue;
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key + ".keyword", entry.getValue()));
        }
        if (StringUtils.hasText(keyword)) {
            MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", keyword);
            titleMP.boost(2);
            MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", keyword);
            textContentMP.boost(1);
            boolQueryBuilder.should(titleMP).should(textContentMP);

            boolQueryBuilder.minimumShouldMatch(1);
            if (isNeedHighlight) {
                HighlightBuilder highlightBuilder = this.buildHighlightBuilder();
                sourceBuilder.highlighter(highlightBuilder);
            }
        }
        sourceBuilder.from(condition.getPageFrom()).size(condition.getPageSize());

        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.sort("date", SortOrder.DESC);

        request.source(sourceBuilder);
        return request;
    }


    public SearchRequest getDefaultTagsSearchRequest(SearchTagsBaseCondition condition) {

        SearchRequest request = new SearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(condition.getCategory())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword", condition.getCategory()));
        }
        if (condition.getCondition() != null) {
            Map<String, Object> conditionMap = ObjectMapperUtil.toMap(JSONObject.toJSONString(condition.getCondition()));
            for (Map.Entry<String, Object> entry : conditionMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (Objects.nonNull(key) && Objects.nonNull(value)) {
                    boolQueryBuilder.filter(QueryBuilders.termQuery(key + ".keyword", value));
                }
            }
        }
        BucketOrder bucketOrder = BucketOrder.key(false);
        sourceBuilder.aggregation(AggregationBuilders.terms("data").field(condition.getWant() + ".keyword").size(10000).order(bucketOrder));
        sourceBuilder.query(boolQueryBuilder);
        request.source(sourceBuilder);
        return request;
    }


    public SearchRequest getDivideSortSearch(SearchSortBaseCondition condition) {

        return getDefaultSortSearchRequest(condition, Boolean.FALSE);
    }


    public SearchRequest getDivideDocsSearch(DivideDocsBaseCondition condition) {
        SearchRequest request = getDefaultSearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword", condition.getType()));

        if (StringUtils.hasText(condition.getVersion())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("version.keyword", condition.getVersion()));
        }
        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));
        String keyword = condition.getKeyword();
        MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", keyword).analyzer("ik_max_word").slop(2);
        ptitleMP.boost(200);
        MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", keyword).analyzer("ik_max_word").slop(2);
        ptextContentMP.boost(100);

        boolQueryBuilder.should(ptitleMP).should(ptextContentMP);

        MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", keyword);
        titleMP.boost(2);
        MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", keyword);
        textContentMP.boost(1);
        boolQueryBuilder.should(titleMP).should(textContentMP);

        boolQueryBuilder.minimumShouldMatch(1);

        sourceBuilder.query(boolQueryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("textContent")
                .field("title")
                .fragmentSize(100)
                .preTags("<span>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(condition.getPageFrom()).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        request.source(sourceBuilder);
        return request;
    }


    private void buildMatchQueriesByConfig(List<EsQueryBuildConfig.MatchQuery> matchQueriesConfig, BoolQueryBuilder boolQueryBuilder, String keyWord) {
        for (EsQueryBuildConfig.MatchQuery matchQueryConfig : matchQueriesConfig) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(matchQueryConfig.getName(), keyWord).analyzer(matchQueryConfig.getAnalyzer());
            if (matchQueryConfig.getBoost() != null && matchQueryConfig.getBoost() > 0) {
                matchQueryBuilder.boost(matchQueryConfig.getBoost());
            }
            boolQueryBuilder.should(matchQueryBuilder);
        }
    }

    private void buildMatchPhraseQueriesrByConfig(List<EsQueryBuildConfig.MatchQuery> matchQueriesConfig, BoolQueryBuilder boolQueryBuilder, String keyWord) {
        for (EsQueryBuildConfig.MatchQuery matchQueryConfig : matchQueriesConfig) {
            MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(matchQueryConfig.getName(), keyWord).analyzer(matchQueryConfig.getAnalyzer()).boost(matchQueryConfig.getBoost() == null ? 1 : matchQueryConfig.getBoost());
            if (matchQueryConfig.getSlop() != null) {
                matchPhraseQueryBuilder.slop(matchQueryConfig.getSlop());
            }
            boolQueryBuilder.should(matchPhraseQueryBuilder);
        }
    }
}
