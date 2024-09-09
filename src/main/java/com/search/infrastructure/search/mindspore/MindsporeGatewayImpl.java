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
package com.search.infrastructure.search.mindspore;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.mindspore.dto.DocsMindsporeCondition;
import com.search.domain.mindspore.dto.SortMindsporeCondition;
import com.search.domain.mindspore.dto.TagsMindsporeCondition;
import com.search.domain.mindspore.gateway.MindSporeGateway;
import com.search.domain.mindspore.vo.MindSporeVo;
import com.search.domain.openmind.vo.OpenMindVo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.mindspore.dataobject.MindsporeDo;
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
public class MindsporeGatewayImpl extends BaseFounctionGateway implements MindSporeGateway {
    /**
     * Search for different types of data.
     *
     * @param searchBaseCondition The search condition for querying MindSpore.
     * @return ResponceResult.
     */
    @Override
    public DocsResponceVo<MindSporeVo> searchByCondition(DocsMindsporeCondition searchBaseCondition) {
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchBaseCondition);
        List<MindsporeDo> mindsporeDos = CommonConverter.toDoList(dateMapList, MindsporeDo.class);
        List<MindSporeVo> mindSporeVos = CommonConverter.toBaseVoList(mindsporeDos, MindSporeVo.class);
        DocsResponceVo<MindSporeVo> docsResponceVo = new DocsResponceVo(mindSporeVos,
                searchBaseCondition.getPageSize(),
                searchBaseCondition.getPage(),
                searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    /**
     * Search the number of   MindSpore data.
     *
     * @param condition The search condition for querying MindSpore.
     * @return CountResponceVo.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsMindsporeCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

    /**
     * Search for sort  of  MindSpore data.
     *
     * @param sortCondition The search condition for MindSpore.
     * @return SortResponceVo<MindSporeVo>.
     */
    @Override
    public SortResponceVo getSearchSortByCondition(SortMindsporeCondition sortCondition) {
        SearchResponse response = super.getSearchSortListByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<MindsporeDo> mindsporeDos = CommonConverter.toDoList(dateMapList, MindsporeDo.class);
            List<MindSporeVo> mindSporeVos = CommonConverter.toBaseVoList(mindsporeDos, MindSporeVo.class);
            long total = 0L;
            if (response.getHits() != null && response.getHits().getTotalHits() != null) {
                total = response.getHits().getTotalHits().value;
            }
            SortResponceVo<MindSporeVo> sortResponceVo = new SortResponceVo(mindSporeVos,
                    sortCondition.getPageSize(),
                    sortCondition.getPage(),
                    total);
            return sortResponceVo;
        }
        return null;
    }

    /**
     * Search the tags of   MindSpore data..
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    /**
     * get Dvide Search Sort  of   MindSpore data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
    @Override
    public SortResponceVo getDvideSearchSortByCondition(SortMindsporeCondition sortCondition) {
        SearchResponse response = super.getDvideSearchSortByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            long total = 0L;
            if (response.getHits() != null && response.getHits().getTotalHits() != null) {
                total = response.getHits().getTotalHits().value;
            }
            SortResponceVo<OpenMindVo> sortResponceVo = new SortResponceVo(openMindVos,
                    sortCondition.getPageSize(),
                    sortCondition.getPage(),
                    total);
            return sortResponceVo;
        }
        return null;
    }

    /**
     * Search for  MindSpore document data.
     *
     * @param condition The search condition for querying different types of data.
     * @return SortResponceVo<OpenEulerVo>.
     */
    @Override
    public SortResponceVo<MindSporeVo> searchDocByType(DivideDocsBaseCondition condition) {
        SearchResponse searchResponse = super.getSearchDocByType(condition);
        if (searchResponse != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(searchResponse);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            long total = 0L;
            if (searchResponse.getHits() != null && searchResponse.getHits().getTotalHits() != null) {
                total = searchResponse.getHits().getTotalHits().value;
            }
            SortResponceVo<MindSporeVo> sortResponceVo = new SortResponceVo(openMindVos, condition.getPageSize(),
                    condition.getPage(),
                    total);
            return sortResponceVo;
        }
        return null;
    }


}
