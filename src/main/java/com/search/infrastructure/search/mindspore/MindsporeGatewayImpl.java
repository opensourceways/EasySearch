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
    @Override
    public DocsResponceVo<MindSporeVo> searchByCondition(DocsMindsporeCondition searchBaseCondition) {
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchBaseCondition);
        List<MindsporeDo> mindsporeDos = CommonConverter.toDoList(dateMapList, MindsporeDo.class);
        List<MindSporeVo> mindSporeVos = CommonConverter.toBaseVoList(mindsporeDos, MindSporeVo.class);
        DocsResponceVo<MindSporeVo> docsResponceVo = new DocsResponceVo(mindSporeVos, searchBaseCondition.getPageSize(), searchBaseCondition.getPage(), searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    @Override
    public CountResponceVo getSearchCountByCondition(DocsMindsporeCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

    @Override
    public SortResponceVo getSearchSortByCondition(SortMindsporeCondition sortCondition) {
        SearchResponse response = super.getSearchSortListByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<MindsporeDo> mindsporeDos = CommonConverter.toDoList(dateMapList, MindsporeDo.class);
            List<MindSporeVo> mindSporeVos = CommonConverter.toBaseVoList(mindsporeDos, MindSporeVo.class);
            SortResponceVo<MindSporeVo> sortResponceVo = new SortResponceVo(mindSporeVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    @Override
    public SortResponceVo getDvideSearchSortByCondition(SortMindsporeCondition sortCondition) {
        SearchResponse response = super.getDvideSearchSortByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            SortResponceVo<OpenMindVo> sortResponceVo = new SortResponceVo(openMindVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    @Override
    public SortResponceVo<MindSporeVo> searchDocByType(DivideDocsBaseCondition condition) {
        SearchResponse searchResponse = super.getSearchDocByType(condition);
        if (searchResponse != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(searchResponse);
            List<OpenMindDo> openMindDos = CommonConverter.toDoList(dateMapList, OpenMindDo.class);
            List<OpenMindVo> openMindVos = CommonConverter.toBaseVoList(openMindDos, OpenMindVo.class);
            SortResponceVo<MindSporeVo> sortResponceVo = new SortResponceVo(openMindVos, condition.getPageSize(), condition.getPage(), searchResponse.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }


}
