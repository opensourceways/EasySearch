package com.search.infrastructure.support.action;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.TagsResponceVo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BaseFounctionGateway {
    @Autowired
    protected RestHighLevelClient restHighLevelClient;
    @Autowired
    protected BaseFounctionBuilder requestBuilder;
    @Autowired
    protected BaseFounctionHandler responceHandler;


    public SearchResponse getSearchDocByType(DivideDocsBaseCondition condition) {
        SearchRequest divideDocsSearch = requestBuilder.getDivideDocsSearch(condition);
        SearchResponse searchResponse = executeDefaultEsSearch(divideDocsSearch);
        return searchResponse;
    }

    public SearchResponse getDvideSearchSortByCondition(SearchSortBaseCondition condition) {
        SearchRequest defaultSortSearchRequest = requestBuilder.getDefaultSortSearchRequest(condition, Boolean.FALSE);
        SearchResponse response = executeDefaultEsSearch(defaultSortSearchRequest);
        return response;
    }


    public SearchResponse getSearchSortListByCondition(SearchSortBaseCondition condition) {
        SearchRequest defaultSortSearchRequest = requestBuilder.getDefaultSortSearchRequest(condition, Boolean.TRUE);
        SearchResponse response = executeDefaultEsSearch(defaultSortSearchRequest);
        return response;
    }


    public List<Map<String, Object>> getDefaultSearchByCondition(SearchDocsBaseCondition condition) {
        SearchRequest defaultSearchRequest = requestBuilder.getDefaultDocsSearchRequest(condition);
        SearchResponse searchResponse = executeDefaultEsSearch(defaultSearchRequest);
        List<Map<String, Object>> dateMapList = responceHandler.getDefaultsHightResponceToMapList(searchResponse, Arrays.asList("title"), "textContent");
        return dateMapList;
    }


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


    protected SearchResponse executeDefaultEsSearch(SearchRequest searchRequest) {
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }


}
