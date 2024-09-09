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
package com.search.infrastructure.search.openeuler;


import com.search.adapter.vo.*;
import com.search.common.util.General;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.openeuler.dto.DocsOpeneulerCondition;
import com.search.domain.openeuler.dto.SortOpeneulerCondition;
import com.search.domain.openeuler.dto.TagsOpeneulerCondition;
import com.search.domain.openeuler.gateway.OpeneulerGateway;
import com.search.domain.openeuler.vo.OpenEulerVo;

import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.openeuler.dataobject.OpenEulerDo;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Slf4j
public class OpeneulerGatewayImpl extends BaseFounctionGateway implements OpeneulerGateway {
    /**
     * Implement search suggestions.
     *
     * @param docsOpeneulerCondition The search condition for querying different types of data.
     * @return SuggResponceVo.
     */
    @Override
    public SuggResponceVo getSuggByCondition(DocsOpeneulerCondition docsOpeneulerCondition) {
        return super.getDefaultSuggByCondition(docsOpeneulerCondition);
    }

    /**
     * Search for different types of  Openeuler data.
     *
     * @param searchBaseCondition The search condition for querying different types of data.
     * @return DocsResponceVo<OpenEulerVo> .
     */
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

    /**
     * Search the number of   Openeuler data.
     *
     * @param condition The search condition for Openeuler.
     * @return CountResponceVo.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsOpeneulerCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

    /**
     * Search for sort  of  Openeuler data.
     *
     * @param sortCondition The search condition for Openeuler.
     * @return SortResponceVo<OpenEulerVo>.
     */
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

    /**
     * Search the tags of   Openeuler data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsOpeneulerCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    /**
     * get Dvide Search Sort  of   Openeuler data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
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

    /**
     * Search for Euler document data
     *
     * @param condition The search condition for querying different types of data.
     * @return SortResponceVo<OpenEulerVo>.
     */
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

    /**
     * 欧拉特殊类型的数据做简单业务处理.
     *
     * @param response elasticsearch  response obj.
     * @return List<Map < String, Object>>.
     */
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
