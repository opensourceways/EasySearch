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
package com.search.infrastructure.search.openmind;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.openmind.dto.DocsOpenmindCondition;
import com.search.domain.openmind.dto.SortOpenmindCondition;
import com.search.domain.openmind.dto.TagsOpenmindCondition;
import com.search.domain.openmind.gateway.OpenmindGateway;
import com.search.domain.openmind.vo.OpenMindVo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.openmind.dataobject.OpenMindDo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class OpenmindGatewayImpl extends BaseFounctionGateway implements OpenmindGateway {
    /**
     * Search for different types of data.
     *
     * @param searchBaseCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    @Override
    public DocsResponceVo<OpenMindVo> searchByCondition(DocsOpenmindCondition searchBaseCondition) {
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchBaseCondition);
        List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
        List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
        DocsResponceVo docsResponceVo = new DocsResponceVo(openMindVos, searchBaseCondition.getPageSize(), searchBaseCondition.getPage(), searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    /**
     * Search the number of data.
     *
     * @param condition The search condition of openmind.
     * @return ResponceResult.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsOpenmindCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

    /**
     * Search for sort  of  Openmind data.
     *
     * @param sortCondition The search condition for Openmind.
     * @return SortResponceVo<OpenMindVo>.
     */
    @Override
    public SortResponceVo getSearchSortByCondition(SortOpenmindCondition sortCondition) {
        SearchResponse response = super.getSearchSortListByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openMindVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    /**
     * Search the tags of   Openmind data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsOpenmindCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    /**
     * get Dvide Search Sort  of   Openmind data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
    @Override
    public SortResponceVo getDvideSearchSortByCondition(SortOpenmindCondition sortCondition) {
        SearchResponse response = super.getDvideSearchSortByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openMindVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    /**
     * Search for  Openmind document data
     *
     * @param condition The search condition .
     * @return SortResponceVo<OpenMindVo>.
     */
    @Override
    public SortResponceVo<OpenMindVo> searchDocByType(DivideDocsBaseCondition condition) {
        SearchResponse searchResponse = super.getSearchDocByType(condition);
        if (searchResponse != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(searchResponse);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openMindVos, condition.getPageSize(), condition.getPage(), searchResponse.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }


}
