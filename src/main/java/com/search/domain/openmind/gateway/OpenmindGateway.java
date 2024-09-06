package com.search.domain.openmind.gateway;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;

import com.search.domain.openmind.dto.DocsOpenmindCondition;
import com.search.domain.openmind.dto.SortOpenmindCondition;
import com.search.domain.openmind.dto.TagsOpenmindCondition;
import com.search.domain.openmind.vo.OpenMindVo;

public interface OpenmindGateway {
    DocsResponceVo<OpenMindVo> searchByCondition(DocsOpenmindCondition searchDocsCondition);

    CountResponceVo getSearchCountByCondition(DocsOpenmindCondition searchDocsCondition);


    SortResponceVo<OpenMindVo> getSearchSortByCondition(SortOpenmindCondition sortCondition);

    TagsResponceVo getSearchTagsByCondition(TagsOpenmindCondition tagsCondition);

    SortResponceVo<OpenMindVo> getDvideSearchSortByCondition(SortOpenmindCondition sortCondition);


    SortResponceVo<OpenMindVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
