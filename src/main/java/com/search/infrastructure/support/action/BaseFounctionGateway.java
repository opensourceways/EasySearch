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
package com.search.infrastructure.support.action;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.SuggResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.common.util.General;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import com.search.domain.base.dto.SearchSortBaseCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;
import com.search.domain.base.vo.TagsVo;
import com.search.infrastructure.support.converter.CommonConverter;;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BaseFounctionGateway {
    /**
     * Autowired RestHighLevelClient bean.
     */
    @Autowired
    protected RestHighLevelClient restHighLevelClient;
    /**
     * Autowired BaseFounctionRequestBuilder bean.
     */
    @Autowired
    protected BaseFounctionRequestBuilder requestBuilder;
    /**
     * Autowired BaseFounctionResponceHandler bean.
     */
    @Autowired
    protected BaseFounctionResponceHandler responceHandler;

    /**
     * Logger for BaseFounctionGateway.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseFounctionGateway.class);

    /**
     * 根据DivideDocsBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param condition The search condition of query es.
     * @return SearchResponse.
     */
    public SearchResponse getSearchDocByType(DivideDocsBaseCondition condition) {
        SearchRequest divideDocsSearch = requestBuilder.getDivideDocsSearch(condition);
        SearchResponse searchResponse = executeDefaultEsSearch(divideDocsSearch);
        return searchResponse;
    }

    /**
     * 根据SearchSortBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param condition The search condition of query es.
     * @return SearchResponse.
     */
    public SearchResponse getDvideSearchSortByCondition(SearchSortBaseCondition condition) {
        SearchRequest defaultSortSearchRequest = requestBuilder.getDefaultSortSearchRequest(condition, Boolean.FALSE);
        SearchResponse response = executeDefaultEsSearch(defaultSortSearchRequest);
        return response;
    }

    /**
     * 根据SearchSortBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param condition The search condition of query es.
     * @return SearchResponse.
     */
    public SearchResponse getSearchSortListByCondition(SearchSortBaseCondition condition) {
        SearchRequest defaultSortSearchRequest = requestBuilder.getDefaultSortSearchRequest(condition, Boolean.TRUE);
        SearchResponse response = executeDefaultEsSearch(defaultSortSearchRequest);
        return response;
    }

    /**
     * 根据SearchSortBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param condition The search condition of query es.
     * @return SearchResponse.
     */

    public List<Map<String, Object>> getDefaultSearchByCondition(SearchDocsBaseCondition condition) {
        SearchRequest defaultSearchRequest = requestBuilder.getDefaultDocsSearchRequest(condition);
        SearchResponse searchResponse = executeDefaultEsSearch(defaultSearchRequest);
        List<Map<String, Object>> dateMapList = responceHandler.getDefaultsHightResponceToMapList(searchResponse, Arrays.asList("title"), "textContent");
        return dateMapList;
    }

    /**
     * 根据SearchDocsBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param docsBaseCondition The search condition of query es.
     * @return SearchResponse.
     */

    public SuggResponceVo getDefaultSuggByCondition(SearchDocsBaseCondition docsBaseCondition) {
        String saveIndex = docsBaseCondition.getIndex();
        SuggResponceVo suggResponceVo = new SuggResponceVo();
        HashSet<String> suggestSet = new HashSet<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestionBuilder<TermSuggestionBuilder> termSuggestionBuilder =
                SuggestBuilders.termSuggestion("textContent")
                        .text(docsBaseCondition.getKeyword())
                        .minWordLength(2)
                        .prefixLength(0)
                        .analyzer("ik_smart")
                        .size(3)
                        .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        String suggFiled = "my_sugg";
        suggestBuilder.addSuggestion(suggFiled, termSuggestionBuilder);

        SearchRequest suggRequest = new SearchRequest(saveIndex);
        suggRequest.source(searchSourceBuilder.suggest(suggestBuilder));

        SearchResponse suggResponse = executeDefaultEsSearch(suggRequest);
        if (suggResponse == null) {
            return suggResponceVo;
        }
        for (int i = 0; i <= 3; i++) {
            StringBuilder sb = new StringBuilder();
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> my_sugg : suggResponse.getSuggest().getSuggestion(suggFiled)) {

                String op = my_sugg.getText().string();

                boolean hc = General.haveChinese(op);

                if (!hc) {
                    if (my_sugg.getOptions().size() > i) {
                        op = my_sugg.getOptions().get(i).getText().string();
                        sb.append("<em>").append(op).append("</em>").append(" ");
                    } else {
                        sb.append(op).append(" ");
                    }
                } else {
                    sb.append(op);
                }
            }
            suggestSet.add(sb.toString().trim());

        }
        suggResponceVo.setSuggestList(suggestSet.stream().toList());
        return suggResponceVo;
    }

    /**
     * 根据SearchTagsBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param tagsCondition The search condition of query es.
     * @return SearchResponse.
     */
    public TagsResponceVo getDefaultSearchTagsByCondition(SearchTagsBaseCondition tagsCondition) {
        TagsResponceVo tagsResponceVo = new TagsResponceVo();
        SearchRequest defaultTagsSearchRequest = requestBuilder.getDefaultTagsSearchRequest(tagsCondition);
        SearchResponse searchResponse = executeDefaultEsSearch(defaultTagsSearchRequest);
        if (searchResponse != null) {
            List<Map<String, Object>> numberList = responceHandler.handAggregationToCountList(searchResponse, "data");
            List<TagsVo> tagsVos = CommonConverter.toTagsVoList(numberList);
            tagsResponceVo.setTotalNum(tagsVos);
        }
        return tagsResponceVo;
    }

    /**
     * 根据SearchDocsBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param condition The search condition of query es.
     * @return SearchResponse.
     */
    public CountResponceVo getDefaultSearchCountByCondition(SearchDocsBaseCondition condition) {
        // Aggregation's field
        String field = "type.keyword";
        String terms = "data";
        SearchRequest defaultCountSearchRequest = requestBuilder.getDefaultCountSearchRequest(condition, field, terms);
        SearchResponse response = executeDefaultEsSearch(defaultCountSearchRequest);
        if (response != null) {
            List<Map<String, Object>> numberList = responceHandler.handAggregationToCountList(response, terms);
            CountResponceVo countResponceVo = new CountResponceVo();
            countResponceVo.setTotal(CommonConverter.toCountVoList(numberList));
            return countResponceVo;
        }
        return null;
    }

    /**
     * 执行search动作.
     *
     * @param searchRequest The search condition of query es.
     * @return SearchResponse.
     */
    protected SearchResponse executeDefaultEsSearch(SearchRequest searchRequest) {
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return response;
    }


}
