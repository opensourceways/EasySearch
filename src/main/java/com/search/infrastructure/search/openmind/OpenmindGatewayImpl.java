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
    @Override
    public DocsResponceVo<OpenMindVo> searchByCondition(DocsOpenmindCondition searchBaseCondition) {
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchBaseCondition);
        List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
        List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
        DocsResponceVo docsResponceVo = new DocsResponceVo(openMindVos, searchBaseCondition.getPageSize(), searchBaseCondition.getPage(), searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    @Override
    public CountResponceVo getSearchCountByCondition(DocsOpenmindCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

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

    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsOpenmindCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

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
