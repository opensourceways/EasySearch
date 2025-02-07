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
package com.search.docsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.docsearch.config.EsfunctionScoreConfig;
import com.search.docsearch.config.MySystem;
import com.search.docsearch.entity.vo.NpsBody;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.entity.vo.SearchTags;
import com.search.docsearch.except.ServiceException;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.factorys.HttpConnectFactory;
import com.search.docsearch.multirecall.composite.DataComposite;
import com.search.docsearch.multirecall.recall.MultiSearchContext;
import com.search.docsearch.multirecall.recall.cstrategy.EsSearchStrategy;
import com.search.docsearch.multirecall.recall.cstrategy.GSearchStrategy;
import com.search.docsearch.properties.FusionSortProperties;
import com.search.docsearch.properties.GoogleSearchProperties;
import com.search.docsearch.service.SearchService;
import com.search.docsearch.utils.General;
import com.search.docsearch.utils.ParameterUtil;
import com.search.docsearch.utils.Trie;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;


@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    @Qualifier("elasticsearchClient")
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    @Qualifier("setConfig")
    private MySystem mySystem;
  
    @Value("${api.allApi}")
    private String allApi;

    @Value("${api.starsApi}")
    private String starsApi;

    @Value("${api.sigNameApi}")
    private String sigNameApi;

    @Value("${api.repoInfoApi}")
    private String repoInfoApi;

    @Value("${api.npsApi}")
    private String npsApi;

    /**
     * insert google serach properties
     */
    @Autowired
    private GoogleSearchProperties gProperties;

    /**
     * insert httpConnectionFactory to creat a URL
     */
    @Autowired
    private HttpConnectFactory httpConnectFactory;

    /**
     * insert fusion sort properties
     */
    @Autowired
    private FusionSortProperties fuProperties;
    
    @Autowired
    private EsfunctionScoreConfig esfunctionScoreConfig;


    @Autowired
    private Trie trie;

    public Map<String, Object> getSuggestion(String keyword, String lang) throws ServiceImplException {
        List<String> suggestList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        result.put("suggestList", suggestList);
        for (int i = 0; i < 3 && i < keyword.length(); i++) {
            suggestList.addAll(trie.searchTopKWithPrefix(keyword.substring(0, keyword.length() - i), 5).stream().map(k-> "<em>"+k.getKey()+"</em>").collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(suggestList))
                break;

        }
        if (CollectionUtils.isEmpty(suggestList)) {
            String suggestCorrection = trie.suggestCorrection(keyword);
            suggestList.addAll(trie.searchTopKWithPrefix(suggestCorrection, 5).stream().map(k-> "<em>"+k.getKey()+"</em>").collect(Collectors.toList()));
        }
        String emKeyWord = "<em>" + keyword + "</em>";
        suggestList = suggestList.stream().filter(a -> !emKeyWord.equals(a)).collect(Collectors.toList());
        if (suggestList.size()>0)
            return result;

        String saveIndex = mySystem.index + "_" + lang;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestionBuilder<TermSuggestionBuilder> termSuggestionBuilder =
                SuggestBuilders.termSuggestion("textContent")
                        .text(keyword)
                        .minWordLength(2)
                        .prefixLength(0)
                        .analyzer("ik_smart")
                        .size(3)
                        .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("my_sugg", termSuggestionBuilder);

        SearchRequest suggRequest = new SearchRequest(saveIndex);

        suggRequest.source(searchSourceBuilder.suggest(suggestBuilder));


        SearchResponse suggResponse = null;
        try {
            suggResponse = restHighLevelClient.search(suggRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }


        for (int i = 0; i <= 3; i++) {
            StringBuilder sb = new StringBuilder();
            boolean isNew = false;
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> my_sugg : suggResponse.getSuggest().getSuggestion("my_sugg")) {

                String op = my_sugg.getText().string();

                boolean hc = General.haveChinese(op);

                if (!hc) {
                    if (my_sugg.getOptions().size() > i) {
                        op = my_sugg.getOptions().get(i).getText().string();
                        isNew = true;
                        sb.append("<em>").append(op).append("</em>").append(" ");
                    } else {
                        sb.append(op).append(" ");
                    }
                } else {
                    sb.append(op);
                }
            }


            if (isNew) {
                suggestList.add(sb.toString().trim());
            }
        }

        result.put("suggestList", suggestList);
        return result;


    }


    /**
     * main page doc search
     *
     * @param condition the user query
     * @return the search results
     */
    @Override
    public Map<String, Object> searchByConditionEs(SearchCondition condition) throws ServiceImplException {
        //create es search strategy
        EsSearchStrategy esRecall = new EsSearchStrategy(restHighLevelClient,mySystem.index,trie,esfunctionScoreConfig,fuProperties);
        MultiSearchContext multirecall = new MultiSearchContext();
        //set es search into search contex
        multirecall.setSearchStrategy(esRecall);
        //do recall and fetch the result
        DataComposite multiRecallRes = multirecall.executeMultiSearch(condition);
        if ("desc".equals(condition.getSort())) {
            return multiRecallRes.getChild(0).getResList();
        }
        multiRecallRes.setFuProperties(fuProperties);
        return multiRecallRes.mergeResult();
    }

    /**
     * multi doc search
     *
     * @param condition the user query
     * @return the search results
     */
    @Override
    public Map<String, Object> searchByConditionMulti(SearchCondition condition) throws ServiceImplException {
        //create es search strategy
        EsSearchStrategy esRecall = new EsSearchStrategy(restHighLevelClient,mySystem.index,trie,esfunctionScoreConfig,fuProperties);
        GSearchStrategy gRecall = new GSearchStrategy(gProperties, httpConnectFactory);
        MultiSearchContext multirecall = new MultiSearchContext();
        //set es search into search contex
        multirecall.setSearchStrategy(esRecall);
        multirecall.setSearchStrategy(gRecall);
        //do recall and fetch the result
        DataComposite multiRecallRes = multirecall.executeMultiSearch(condition);
        if ("desc".equals(condition.getSort())) {
            return multiRecallRes.getChild(0).getResList();
        }
        multiRecallRes.setFuProperties(fuProperties);
        // multiRecallRes.filter("policy")  filtering data here
        return multiRecallRes.mergeResult();
    }

    public SearchRequest BuildSearchRequest(SearchCondition condition, String index) {
        int startIndex = (condition.getPage() - 1) * condition.getPageSize();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.hasText(condition.getType())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("type.keyword",  condition.getType().split(",")));
        }
        //因为会出现一些特殊的字符导致分词出错（比如英文连接词），在这里处理一下
        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        ptitleMP.boost(esfunctionScoreConfig.titleBoost == null ? 1000 : esfunctionScoreConfig.titleBoost);
        MatchPhraseQueryBuilder ph1MP = QueryBuilders.matchPhraseQuery("h1", condition.getKeyword()).analyzer("ik_max_word");
        ph1MP.boost(esfunctionScoreConfig.h1Boost == null ? 900 : esfunctionScoreConfig.h1Boost);
        MatchPhraseQueryBuilder ph2MP = QueryBuilders.matchPhraseQuery("h2", condition.getKeyword()).analyzer("ik_max_word");
        ph2MP.boost(esfunctionScoreConfig.h2Boost == null ? 800 : esfunctionScoreConfig.h2Boost);
        MatchPhraseQueryBuilder ph3MP = QueryBuilders.matchPhraseQuery("h3", condition.getKeyword()).analyzer("ik_max_word");
        ph3MP.boost(esfunctionScoreConfig.h3Boost == null ? 700 : esfunctionScoreConfig.h3Boost);
        MatchPhraseQueryBuilder ph4MP = QueryBuilders.matchPhraseQuery("h4", condition.getKeyword()).analyzer("ik_max_word");
        ph4MP.boost(esfunctionScoreConfig.h4Boost == null ? 600 : esfunctionScoreConfig.h4Boost);
        MatchPhraseQueryBuilder ph5MP = QueryBuilders.matchPhraseQuery("h5", condition.getKeyword()).analyzer("ik_max_word");
        ph5MP.boost(esfunctionScoreConfig.h5Boost == null ? 500 : esfunctionScoreConfig.h5Boost);
        MatchPhraseQueryBuilder pstrongMP = QueryBuilders.matchPhraseQuery("strong", condition.getKeyword()).analyzer("ik_max_word");
        pstrongMP.boost(esfunctionScoreConfig.strongBoost == null ? 150 : esfunctionScoreConfig.strongBoost);

        MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        ptextContentMP.boost(esfunctionScoreConfig.textContentBoost == null ? 100 : esfunctionScoreConfig.textContentBoost);

        boolQueryBuilder.should(ptitleMP).should(ph1MP).should(ph2MP).should(ph3MP).should(ph4MP).should(ph5MP).should(pstrongMP).should(ptextContentMP);
        MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", condition.getKeyword()).analyzer("ik_smart");
        titleMP.boost(2);
        MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", condition.getKeyword()).analyzer("ik_smart");
        textContentMP.boost(1);
        boolQueryBuilder.should(titleMP).should(textContentMP);

        boolQueryBuilder.minimumShouldMatch(1);

        if (esfunctionScoreConfig.limitType == null || StringUtils.isEmpty(condition.getType()) || esfunctionScoreConfig.limitType.contains(condition.getType()))
            if (condition.getLimit() != null) {
                for (Map<String, String> map : condition.getLimit()) {
                    BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (key.equals("version")) {
                            String[] versions = value.split(",");
                            vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", versions));
                        } else {
                            vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                        }
                    }
                    boolQueryBuilder.mustNot(vBuilder);
                }
            }

        if (condition.getFilter() != null) {
            BoolQueryBuilder zBuilder = QueryBuilders.boolQuery();
            for (Map<String, String> map : condition.getFilter()) {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("version")) {
                        String[] versions = value.split(",");
                        vBuilder.must(QueryBuilders.termsQuery("version.keyword", versions));
                    } else {
                        vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                    }
                }
                zBuilder.should(vBuilder);
            }
            boolQueryBuilder.filter(zBuilder);
        }

        if ((condition.getType() == null || "".equals(condition.getType().trim())) && esfunctionScoreConfig.functionscore != null && esfunctionScoreConfig.functionscore.size() > 0) {
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionBuilder = new FunctionScoreQueryBuilder.FilterFunctionBuilder[esfunctionScoreConfig.functionscore.size()];
            for (int i = 0; i < esfunctionScoreConfig.functionscore.size(); i++) {
                Map<String, Object> eachFilter = esfunctionScoreConfig.functionscore.get(i);
                functionBuilder[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery(eachFilter.get("termkey").toString(), eachFilter.get("value")), ScoreFunctionBuilders.weightFactorFunction(Float.parseFloat(String.valueOf(eachFilter.get("weight")))));
            }
            FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQueryBuilder, functionBuilder)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .boostMode(CombineFunction.MULTIPLY);
            sourceBuilder.query(functionScoreQuery);

        } else {
            sourceBuilder.query(boolQueryBuilder);
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("textContent")
                .field("title")
                .fragmentSize(100)
                .preTags("<span>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(startIndex).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        if ("desc".equals(condition.getSort())) {
            sourceBuilder.sort("date", SortOrder.DESC);
        }
        request.source(sourceBuilder);
        return request;
    }


    public Map<String, Object> getCount(SearchCondition condition) throws ServiceImplException {
        String saveIndex = mySystem.index + "_" + condition.getLang();
        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //因为会出现一些特殊的字符导致分词出错（比如英文连接词），在这里处理一下
        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        ptitleMP.boost(esfunctionScoreConfig.titleBoost == null ? 1000 : esfunctionScoreConfig.titleBoost);
        MatchPhraseQueryBuilder ph1MP = QueryBuilders.matchPhraseQuery("h1", condition.getKeyword()).analyzer("ik_max_word");
        ph1MP.boost(esfunctionScoreConfig.h1Boost == null ? 900 : esfunctionScoreConfig.h1Boost);
        MatchPhraseQueryBuilder ph2MP = QueryBuilders.matchPhraseQuery("h2", condition.getKeyword()).analyzer("ik_max_word");
        ph2MP.boost(esfunctionScoreConfig.h2Boost == null ? 800 : esfunctionScoreConfig.h2Boost);
        MatchPhraseQueryBuilder ph3MP = QueryBuilders.matchPhraseQuery("h3", condition.getKeyword()).analyzer("ik_max_word");
        ph3MP.boost(esfunctionScoreConfig.h3Boost == null ? 700 : esfunctionScoreConfig.h3Boost);
        MatchPhraseQueryBuilder ph4MP = QueryBuilders.matchPhraseQuery("h4", condition.getKeyword()).analyzer("ik_max_word");
        ph4MP.boost(esfunctionScoreConfig.h4Boost == null ? 600 : esfunctionScoreConfig.h4Boost);
        MatchPhraseQueryBuilder ph5MP = QueryBuilders.matchPhraseQuery("h5", condition.getKeyword()).analyzer("ik_max_word");
        ph5MP.boost(esfunctionScoreConfig.h5Boost == null ? 500 : esfunctionScoreConfig.h5Boost);
        MatchPhraseQueryBuilder pstrongMP = QueryBuilders.matchPhraseQuery("strong", condition.getKeyword()).analyzer("ik_max_word");
        pstrongMP.boost(esfunctionScoreConfig.strongBoost == null ? 150 : esfunctionScoreConfig.strongBoost);

        MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        ptextContentMP.boost(esfunctionScoreConfig.textContentBoost == null ? 100 : esfunctionScoreConfig.textContentBoost);

        boolQueryBuilder.should(ptitleMP).should(ph1MP).should(ph2MP).should(ph3MP).should(ph4MP).should(ph5MP).should(pstrongMP).should(ptextContentMP);
        MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", condition.getKeyword()).analyzer("ik_smart");
        titleMP.boost(2);
        MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", condition.getKeyword()).analyzer("ik_smart");
        textContentMP.boost(1);
        boolQueryBuilder.should(titleMP).should(textContentMP);

        boolQueryBuilder.minimumShouldMatch(1);

        if (esfunctionScoreConfig.limitType == null || StringUtils.isEmpty(condition.getType()) || esfunctionScoreConfig.limitType.contains(condition.getType()))
            if (condition.getLimit() != null) {
                for (Map<String, String> map : condition.getLimit()) {
                    BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (key.equals("version")) {
                            String[] versions = value.split(",");
                            vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", versions));
                        } else {
                            vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                        }
                    }
                    boolQueryBuilder.mustNot(vBuilder);
                }
            }

        if (condition.getFilter() != null) {
            BoolQueryBuilder zBuilder = QueryBuilders.boolQuery();
            for (Map<String, String> map : condition.getFilter()) {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("version")) {
                        String[] versions = value.split(",");
                        vBuilder.must(QueryBuilders.termsQuery("version.keyword", versions));
                    } else {
                        vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                    }
                }
                zBuilder.should(vBuilder);
            }
            boolQueryBuilder.filter(zBuilder);
        }

        if ((condition.getType() == null || "".equals(condition.getType().trim())) && esfunctionScoreConfig.functionscore != null && esfunctionScoreConfig.functionscore.size() > 0) {
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionBuilder = new FunctionScoreQueryBuilder.FilterFunctionBuilder[esfunctionScoreConfig.functionscore.size()];
            for (int i = 0; i < esfunctionScoreConfig.functionscore.size(); i++) {
                Map<String, Object> eachFilter = esfunctionScoreConfig.functionscore.get(i);
                functionBuilder[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery(eachFilter.get("termkey").toString(), eachFilter.get("value")), ScoreFunctionBuilders.weightFactorFunction(Float.parseFloat(String.valueOf(eachFilter.get("weight")))));
            }
            FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQueryBuilder, functionBuilder)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .boostMode(CombineFunction.MULTIPLY);
            sourceBuilder.query(functionScoreQuery);

        } else {
            sourceBuilder.query(boolQueryBuilder);
        }

        sourceBuilder.aggregation(AggregationBuilders.terms("data").field("type.keyword").size(100));
        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        List<Map<String, Object>> numberList = new ArrayList<>();
        Map<String, Object> numberMap = new HashMap<>();
        numberMap.put("doc_count", response.getHits().getTotalHits().value);
        numberMap.put("key", "all");
        numberList.add(numberMap);
        ParsedTerms aggregation = response.getAggregations().get("data");
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            Map<String, Object> countMap = new HashMap<>();
            countMap.put("key", bucket.getKeyAsString());
            countMap.put("doc_count", bucket.getDocCount());
            numberList.add(countMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", numberList);
        return result;
    }

    @Override
    public Map<String, Object> advancedSearch(Map<String, String> search) throws ServiceImplException {
        String saveIndex;
        String lang = search.get("lang");
        if (lang != null) {
            saveIndex = mySystem.index + "_" + lang;
        } else {
            //在没有传语言时默认为zh
            saveIndex = mySystem.index + "_zh";
        }
        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        int page = 1;
        int pageSize = 10;
        String keyword = "";
        for (Map.Entry<String, String> entry : search.entrySet()) {
            if (entry.getKey().equals("page")) {
                page = Integer.parseInt(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("pageSize")) {
                pageSize = Integer.parseInt(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("keyword")) {
                keyword = entry.getValue();
                continue;
            }

            boolQueryBuilder.filter(QueryBuilders.termQuery(entry.getKey() + ".keyword", entry.getValue()));
        }

        int startIndex = (page - 1) * pageSize;

        if (!keyword.equals("")) {
            MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", keyword);
            titleMP.boost(2);
            MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", keyword);
            textContentMP.boost(1);
            boolQueryBuilder.should(titleMP).should(textContentMP);

            boolQueryBuilder.minimumShouldMatch(1);

            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("textContent")
                    .field("title")
                    .fragmentSize(100)
                    .preTags("<span>")
                    .postTags("</span>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        sourceBuilder.from(startIndex).size(pageSize);

        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.sort("date", SortOrder.DESC);

        request.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();

            data.add(map);
        }
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", response.getHits().getTotalHits().value);
        result.put("records", data);
        return result;
    }

    @Override
    public Map<String, Object> getTags(SearchTags searchTags) throws ServiceImplException {
        String saveIndex = mySystem.index + "_" + searchTags.getLang();

        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(searchTags.getCategory())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword", searchTags.getCategory()));
        }

        if (searchTags.getCondition() != null) {
            for (Map.Entry<String, String> entry : searchTags.getCondition().entrySet()) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(entry.getKey() + ".keyword", entry.getValue()));
            }
        }

        BucketOrder bucketOrder = BucketOrder.key(false);
        sourceBuilder.aggregation(AggregationBuilders.terms("data").field(searchTags.getWant() + ".keyword").size(10000).order(bucketOrder));
        sourceBuilder.query(boolQueryBuilder);
        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        ParsedTerms aggregation = response.getAggregations().get("data");
        List<Map<String, Object>> numberList = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {

            Map<String, Object> countMap = new HashMap<>();

            countMap.put("key", bucket.getKeyAsString());
            countMap.put("count", bucket.getDocCount());
            numberList.add(countMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalNum", numberList);
        return result;
    }

    @Override
    public void saveWord() throws ServiceException, IOException {

        int scrollSize = 500;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(scrollSize);
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10));
        SearchRequest searchRequest = new SearchRequest(mySystem.searchWordIndex);
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(scroll);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            String searchWord = map.get("searchWord").toString();
            long searchCount = ((Integer) map.get("searchCount")).longValue();

            trie.insert(searchWord, searchCount);
        }

        while (hits.length > 0) {
            SearchScrollRequest searchScrollRequestS = new SearchScrollRequest(scrollId);
            searchScrollRequestS.scroll(scroll);
            SearchResponse searchScrollResponseS = restHighLevelClient.scroll(searchScrollRequestS, RequestOptions.DEFAULT);
            scrollId = searchScrollResponseS.getScrollId();

            hits = searchScrollResponseS.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> map = hit.getSourceAsMap();
                String searchWord = map.get("searchWord").toString();
                long searchCount = ((Integer) map.get("searchCount")).longValue();

                trie.insert(searchWord, searchCount);
            }
        }
        trie.sortSearchWorld();
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);

        restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        logger.info("Search example updated");
    }

    @Override
    public Map<String, Object> findWord(String prefix) throws ServiceException {
        List<Trie.KeyCountResult> keyCountResultList = new ArrayList<>();
        for (int i = 0; i < 3 && i<prefix.length() ; i++) {
            keyCountResultList.addAll(trie.searchTopKWithPrefix(prefix.substring(0,prefix.length()-i), 10));
            if(!CollectionUtils.isEmpty(keyCountResultList))
                break;
        }
        //没查到根据相似度匹配
        if (CollectionUtils.isEmpty(keyCountResultList)) {
            String suggestCorrection = trie.suggestCorrection(prefix);
            keyCountResultList.addAll(trie.searchTopKWithPrefix(suggestCorrection, 10));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("word", keyCountResultList);

        return result;
    }

    @Override
    public String querySigName(String lang) throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, sigNameApi, community, lang);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }

    @Override
    public String queryAll() throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, allApi, community);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }

    @Override
    public String queryStars() throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, starsApi, community);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }

    @Override
    public String querySigReadme(String sig, String lang) throws ServiceImplException {
        lang = ParameterUtil.vaildLang(lang);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode sigNameList = objectMapper.readTree(querySigName(lang));
            if (sigNameList.get("data") == null || sigNameList.get("data").get("SIG_list") == null) {
                throw new IllegalArgumentException("Invalid sig parameter");
            }
            String urlStr = "";
            for (JsonNode bucket : sigNameList.get("data").get("SIG_list")) {
                if (bucket.get("name").asText().equalsIgnoreCase(sig)) {
                    urlStr = bucket.get("links").asText().replace("/blob/", "/raw/").replace("/tree/", "/raw/");
                }
            }
            return httpRequest(urlStr, true);
        } catch (Exception e) {
            throw new ServiceImplException("can not search");
        }
    }

    @Override
    public String getEcosystemRepoInfo(String ecosystemType, String page, String lang) throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, repoInfoApi, community, ecosystemType, page, lang);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }


    @Override
    public String getNps(String community, NpsBody body) throws ServiceImplException {
        community = ParameterUtil.vaildCommunity(community);
        String urlStr = String.format(npsApi, community);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String bodyStr = objectMapper.writeValueAsString(body);
            return postRequest(urlStr, bodyStr);
        } catch (JsonProcessingException e) {
            throw new ServiceImplException("can not process json");
        } catch (Exception e) {
            throw new ServiceImplException("can not post data");
        }
    }


    public String httpRequest(String urlStr, Boolean flag) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int timeout = 15000; // 设置超时时间为15秒
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8))) {
            String line;
            StringJoiner response = new StringJoiner(flag ? "\n" : "");
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            return response.toString();
        } finally {
            connection.disconnect(); // 断开连接
        }
    }

    public String postRequest(String urlStr, String body) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        int timeout = 15000; // 设置超时时间为15秒
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writer.write(body);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect(); // 断开连接
        }
    }

}
