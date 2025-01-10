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
package com.search.docsearch.multirecall.recall.cstrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
import com.search.docsearch.utils.Trie;
import com.search.docsearch.config.EsfunctionScoreConfig;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.cdata.EsRecallData;
import com.search.docsearch.multirecall.recall.SearchStrategy;
import com.search.docsearch.utils.General;
import org.elasticsearch.client.RestHighLevelClient;


public class EsSearchStrategy implements SearchStrategy {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchStrategy.class);

    /**
     * return the recall list to client
     * 
     */
    private RestHighLevelClient restHighLevelClient;

    /**
     * es index 
     * 
     */
    private String index;

    /**
     * algorithm util
     * 
     */
    private Trie trie;

    /**
     * boost socre config, ranking the recall list
     * 
     */
    private EsfunctionScoreConfig esfunctionScoreConfig;

    /**
     * roughly filter the recalled results
     * 
     * @param pararestHighLevelClient paraClient mannage by spring aoc
     * @param paraindex the index of es search 
     * @param paratire the algorithim toolkit
     * @param config the boost socre config which used to ranking the result list
     */
    public EsSearchStrategy(RestHighLevelClient pararestHighLevelClient, String paraindex, Trie paratire,EsfunctionScoreConfig config){
        this.restHighLevelClient = pararestHighLevelClient;
        this.index = paraindex;
        this.trie = paratire;
        this.esfunctionScoreConfig = config;
    }

    /**
     * wrapper of the funcition search 
     * 
     * @param SearchCondition the user query
     */
    @Override
    public Component search(SearchCondition condition) {
        EsRecallData emptyRes = new EsRecallData(Collections.emptyMap()); //空返回
        try{
            Component EsRecallData = searchByCondition(condition);
            return EsRecallData == null ? emptyRes : EsRecallData; //遇到正常0召回情况，返回空结果
        } catch (ServiceImplException e){ // 遇到异常不中断 返回空结果
            return emptyRes;
        }
    }

    /**
     * doing the recall according user query 
     * 
     * @param SearchCondition the user query
     */
    private Component searchByCondition(SearchCondition condition) throws ServiceImplException {
        String saveIndex = index + "_" + condition.getLang();

        Map<String, Object> result = new HashMap<>();
        result.put("keyword", HtmlUtils.htmlEscape(condition.getKeyword()));

        SearchRequest request = BuildSearchRequest(condition, saveIndex);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        List<Map<String, Object>> data = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            String text = (String) map.getOrDefault("textContent", "");
            if (null != text && text.length() > 200) {
                text = text.substring(0, 200) + "......";
            }
            map.put("textContent", text);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("textContent")) {
                StringBuilder highLight = new StringBuilder();
                for (Text textContent : highlightFields.get("textContent").getFragments()) {
                    highLight.append(textContent.toString()).append("<br>");
                }
                map.put("textContent", highLight.toString());
            }
            if ("whitepaper".equals(map.getOrDefault("type", "")) && !map.containsKey("title")) {
                map.put("title", map.get("secondaryTitle"));
            }
            if ("service".equals(map.getOrDefault("type", ""))) {
                map.put("secondaryTitle", map.get("textContent"));
            }
            if (condition.getPage() == 1) {
                Float score = hit.getScore();
                Double scoreTopSearch = score * trie.getWordSimilarityWithTopSearch(String.valueOf(map.get("title")), 10);
                if (!(Math.abs(scoreTopSearch - 0.0) < 1e-6f)) {
                    map.put("score", scoreTopSearch);
                }
                else {
                    map.put("score", score*1.0);
                }
            }
            if (highlightFields.containsKey("title")) {
                map.put("title", highlightFields.get("title").getFragments()[0].toString());
            }

            data.add(map);
        }
        if (data.isEmpty()) {
            return null;
        }
        if (condition.getPage() == 1) {
            data = data.stream().sorted((a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score"))).collect(Collectors.toList());
        }
        result.put("page", condition.getPage());
        result.put("pageSize", condition.getPageSize());
        result.put("records", data);

        EsRecallData resData = new EsRecallData(result);
        
        return resData;
    }

    /**
     * build the es qeury from search condition 
     * 
     * @param condition the user query
     * @param index the search index
     */
    private SearchRequest BuildSearchRequest(SearchCondition condition, String index) {
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
        request.source(sourceBuilder);
        return request;
    }
}
