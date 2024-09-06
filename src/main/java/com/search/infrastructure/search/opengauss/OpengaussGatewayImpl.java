package com.search.infrastructure.search.opengauss;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.base.vo.TagsVo;
import com.search.domain.openeuler.vo.OpenEulerVo;
import com.search.domain.opengauss.dto.DocsOpengaussCondition;
import com.search.domain.opengauss.dto.SortOpengaussCondition;
import com.search.domain.opengauss.dto.TagsOpengaussCondition;
import com.search.domain.opengauss.gateway.OpengaussGateway;
import com.search.domain.opengauss.vo.OpenGaussVo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.opengauss.dataobject.OpenGaussDo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpengaussGatewayImpl extends BaseFounctionGateway implements OpengaussGateway {


    @Override
    public DocsResponceVo<OpenGaussVo> searchByCondition(DocsOpengaussCondition searchDocsCondition) {
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchDocsCondition);
        List<OpenGaussDo> openGaussDos = CommonConverter.toDoList(dateMapList, OpenGaussDo.class);
        List<OpenGaussVo> openGaussVos = CommonConverter.toBaseVoList(openGaussDos, OpenGaussVo.class);
        DocsResponceVo docsResponceVo = new DocsResponceVo(openGaussVos, searchDocsCondition.getPageSize(), searchDocsCondition.getPage(), searchDocsCondition.getKeyword());
        return docsResponceVo;
    }

    @Override
    public CountResponceVo getSearchCountByCondition(DocsOpengaussCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }

    @Override
    public SortResponceVo<OpenGaussVo> getSearchSortByCondition(SortOpengaussCondition sortCondition) {
        SearchResponse response = super.getSearchSortListByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenGaussDo> openGaussDos = CommonConverter.toDoList(dateMapList, OpenGaussDo.class);
            List<OpenGaussVo> openGaussVos = CommonConverter.toBaseVoList(openGaussDos, OpenGaussVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openGaussVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsOpengaussCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    @Override
    public SortResponceVo<OpenEulerVo> getDvideSearchSortByCondition(SortOpengaussCondition sortCondition) {
        SearchResponse response = super.getDvideSearchSortByCondition(sortCondition);
        if (response != null) {
            List<Map<String, Object>> dateMapList = responceHandler.handResponceHitsToMapList(response);
            List<OpenGaussDo> openGaussDos = CommonConverter.toDoList(dateMapList, OpenGaussDo.class);
            List<OpenGaussVo> openGaussVos = CommonConverter.toBaseVoList(openGaussDos, OpenGaussVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openGaussVos, sortCondition.getPageSize(), sortCondition.getPage(), response.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }

    @Override
    public SortResponceVo<OpenEulerVo> searchDocByType(DivideDocsBaseCondition condition) {
        SearchRequest divideDocsSearch = requestBuilder.getDivideDocsSearch(condition);
        SearchResponse searchResponse = super.executeDefaultEsSearch(divideDocsSearch);
        if (searchResponse != null) {
            List<Map<String, Object>> dateMapList = responceHandler.getDefaultsHightResponceToMapList(searchResponse, Arrays.asList("title"), "textContent");
            if (dateMapList.size() > 2) {
                TagsOpengaussCondition searchTags = new TagsOpengaussCondition();
                searchTags.setCategory("docs");
                searchTags.setIndex(condition.getIndex());
                searchTags.setWant("version");
                TagsResponceVo searchTagsByCondition = this.getSearchTagsByCondition(searchTags);
                ArrayList<String> versionList = new ArrayList<>();
                if (Objects.nonNull(searchTagsByCondition)) {
                    List<TagsVo> totalNum = searchTagsByCondition.getTotalNum();
                    totalNum.stream().forEach(tagsVo -> {
                        versionList.add(tagsVo.getKey());
                    });
                }
                ArrayList<Map> scoreList = new ArrayList<>();
                Map<String, List<Map<String, Object>>> articleName = dateMapList.stream().collect(Collectors.groupingBy(m -> String.valueOf(m.get("articleName"))));
                articleName.forEach((k, v) -> {
                    v.sort((u1, u2) -> Float.compare(Float.parseFloat(String.valueOf(u2.get("score"))), Float.parseFloat(String.valueOf(u1.get("score")))));
                    scoreList.add(v.get(0));
                    v.sort(Comparator.comparingInt(u -> versionList.indexOf(String.valueOf(u.get("version")))));
                });
                scoreList.sort((u1, u2) -> Float.compare(Float.parseFloat(String.valueOf(u2.get("score"))), Float.parseFloat(String.valueOf(u1.get("score")))));
                dateMapList.clear();
                scoreList.stream().forEach(a -> {
                    dateMapList.addAll(articleName.get(a.get("articleName")));
                });
            }
            List<OpenGaussDo> openGaussDos = CommonConverter.toDoList(dateMapList, OpenGaussDo.class);
            List<OpenGaussVo> openGaussVos = CommonConverter.toBaseVoList(openGaussDos, OpenGaussVo.class);
            SortResponceVo sortResponceVo = new SortResponceVo(openGaussVos, condition.getPageSize(), condition.getPage(), searchResponse.getHits().getTotalHits().value);
            return sortResponceVo;
        }
        return null;
    }
}
