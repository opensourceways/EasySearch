package com.search.infrastructure.search.openeuler;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.openeuler.dto.DocsOpeneulerCondition;
import com.search.domain.openeuler.dto.SortOpeneulerCondition;
import com.search.domain.openeuler.dto.TagsOpeneulerCondition;
import com.search.domain.openeuler.gateway.OpeneulerGateway;
import com.search.domain.openeuler.vo.OpenEulerVo;

import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.openeuler.dataobject.OpenEulerDo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class OpeneulerGatewayImpl extends BaseFounctionGateway implements OpeneulerGateway {
    @Override
    public DocsResponceVo<OpenEulerVo> searchByCondition(DocsOpeneulerCondition searchBaseCondition) {
        SearchRequest defaultSearchRequest = requestBuilder.getDefaultDocsSearchRequest(searchBaseCondition);
        SearchResponse searchResponse = executeDefaultEsSearch(defaultSearchRequest);
        List<Map<String, Object>> dateMapList = handDocsEsResponce(searchResponse);
        List<OpenEulerDo> openEulerDos = CommonConverter.toDoList(dateMapList, OpenEulerDo.class);
        List<OpenEulerVo> openEulerVos = CommonConverter.toBaseVoList(openEulerDos, OpenEulerVo.class);
        DocsResponceVo docsResponceVo = new DocsResponceVo(openEulerVos, searchBaseCondition.getPageSize(), searchBaseCondition.getPage(), searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    @Override
    public CountResponceVo getSearchCountByCondition(DocsOpeneulerCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

    @Override
    public SortResponceVo getSearchSortByCondition(SortOpeneulerCondition sortCondition) {
        SearchResponse response = super.getSearchSortListByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenEulerDo> openEulerDos = CommonConverter.toDoList(dateMapList, OpenEulerDo.class);
            List<OpenEulerVo> openEulerVos = CommonConverter.toBaseVoList(openEulerDos, OpenEulerVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openEulerVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsOpeneulerCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    @Override
    public SortResponceVo getDvideSearchSortByCondition(SortOpeneulerCondition sortCondition) {
        SearchResponse response = super.getDvideSearchSortByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenEulerDo> openEulerDos = CommonConverter.toDoList(dateMapList, OpenEulerDo.class);
            List<OpenEulerVo> openEulerVos = CommonConverter.toBaseVoList(openEulerDos, OpenEulerVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openEulerVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    @Override
    public SortResponceVo<OpenEulerVo> searchDocByType(DivideDocsBaseCondition condition) {
        SearchResponse searchResponse = super.getSearchDocByType(condition);
        if (searchResponse != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(searchResponse);
            List<OpenEulerDo> openEulerDos = CommonConverter.toDoList(dateMapList, OpenEulerDo.class);
            List<OpenEulerVo> openEulerVos = CommonConverter.toBaseVoList(openEulerDos, OpenEulerVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openEulerVos, condition.getPageSize(), condition.getPage(), searchResponse.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }


    private List<Map<String, Object>> handDocsEsResponce(SearchResponse response) {
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
            if (highlightFields.containsKey("title")) {
                map.put("title", highlightFields.get("title").getFragments()[0].toString());
            }

            data.add(map);
        }
        return data;
    }

}
