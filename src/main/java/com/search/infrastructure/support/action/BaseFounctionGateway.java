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
import com.search.common.util.Trie;
import com.search.domain.base.dto.SearchSuggBaseCondition;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import com.search.domain.base.dto.SearchSortBaseCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;
import com.search.domain.base.vo.TagsVo;
import com.search.infrastructure.support.converter.CommonConverter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
        List<Map<String, Object>> dateMapList = responceHandler.getDefaultsHightResponceToMapList(
                searchResponse, Arrays.asList("title"), "textContent");
        return dateMapList;
    }

    /**
     * 根据SearchDocsBaseCondition组装SearchRequest 并执行search动作.
     *
     * @param suggBaseCondition The search condition of query es.
     * @return SearchResponse.
     */

    public SuggResponceVo getDefaultSuggByCondition(SearchSuggBaseCondition suggBaseCondition) {

        SuggResponceVo suggResponceVo = new SuggResponceVo();
        SearchRequest suggRequest = requestBuilder.getDefaultSuggSearchRequest(suggBaseCondition);
        SearchResponse suggResponse = executeDefaultEsSearch(suggRequest);
        if (suggResponse == null) {
            return suggResponceVo;
        }
        List<String> suggestList = responceHandler.handSuggResponceToList(suggResponse, suggBaseCondition.getFieldname());
        if (suggestList.size() > 0) {
            for (int i = 0; i < suggestList.size(); i++) {
                StringBuilder originBuilder = new StringBuilder();
                originBuilder.append("<em>").append(suggestList.get(i)).append("</em>").append(" ");
                suggestList.set(i, originBuilder.toString());
            }
        }
        suggResponceVo.setSuggestList(suggestList);
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
            List<Map<String, Object>> numberList = responceHandler.handAggregationToCountList(searchResponse, "data", Boolean.FALSE);
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
            List<Map<String, Object>> numberList = responceHandler.handAggregationToCountList(response, terms, Boolean.TRUE);
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
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return response;
    }

    /**
     * 某一字段的值聚合.
     *
     * @param field 聚合的字段.
     * @param index 索引.
     * @return List<TagsVo>.
     */
    public List<TagsVo> aggFieldCount(String field, String index) {
        List<TagsVo> tagsVoList = new ArrayList<>();
        Map<String, Object> afterKey = null;
        boolean hasMorePages = true;
        while (hasMorePages) {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            CompositeAggregationBuilder compositeAggregation = AggregationBuilders.composite("unique_fields", List.of(
                    new TermsValuesSourceBuilder(field)
                            .field(field + ".keyword")
            )).size(1000);

            if (afterKey != null) {
                compositeAggregation.aggregateAfter(afterKey);
            }

            searchSourceBuilder.aggregation(compositeAggregation);
            searchSourceBuilder.size(0);

            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = executeDefaultEsSearch(searchRequest);

            if (searchResponse != null) {
                CompositeAggregation aggregation = searchResponse.getAggregations().get("unique_fields");

                for (CompositeAggregation.Bucket bucket : aggregation.getBuckets()) {
                    TagsVo tagsVo = new TagsVo();
                    tagsVo.setCount(bucket.getDocCount());
                    tagsVo.setKey(String.valueOf(bucket.getKey().get(field)));
                    tagsVoList.add(tagsVo);
                }

                afterKey = aggregation.afterKey();
                hasMorePages = aggregation.getBuckets().size() > 0;
            } else {
                hasMorePages = false;
            }


        }
        return tagsVoList;
    }


    /**
     * init Trie.
     *
     * @param index index.
     * @param trieMap trieMap.
     * @return Trie.
     */
    protected Trie initTrie(String index, Map<String, Trie> trieMap) {
        List<TagsVo> tagsVoList = aggFieldCount("title", index);
        System.out.println(tagsVoList);
        Trie trie = new Trie();
        for (TagsVo a : tagsVoList) {
            trie.insert(a.getKey(), a.getCount().intValue());
            String lowerCaseKey = a.getKey().toLowerCase(Locale.ROOT);
            if (!lowerCaseKey.equals(a.getKey())) {
                trie.insert(lowerCaseKey, a.getCount().intValue());
            }
        }
        trieMap.put(index, trie);
        trie.sortSearchWorld();
        return trie;
    }
    /**
     * getTrie.
     * @param  input input.
     * @param index index.
     * @param trieMap trieMap.
     * @return Trie.
     */
    protected List<TagsVo> getTrie(String input, String index, Map<String, Trie> trieMap) {
        List<TagsVo> keyCountResultList = new ArrayList<>();
        Trie trie = trieMap.get(index);
        if (trie == null || trie.getSearchCountMap().size() == 0) {
            trie = initTrie(index, trieMap);
        }
        String prefix = input;
        for (int i = 0; i < 3 && i < prefix.length(); i++) {
            String substring = prefix.substring(0, prefix.length() - i);
            keyCountResultList = trie.searchTopKWithPrefix(substring, 10);
            if (!CollectionUtils.isEmpty(keyCountResultList)) {
                break;
            }
        }
        if (CollectionUtils.isEmpty(keyCountResultList)) {
            String newPrefix = General.replacementCharacter(prefix);
            keyCountResultList = trie.searchTopKWithPrefix(newPrefix, 10);
        }
        return keyCountResultList;
    }
}
