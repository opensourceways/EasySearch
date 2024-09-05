package com.search.domain.openeuler.gateway;

import com.search.adapter.vo.*;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.openeuler.dto.DocsOpeneulerCondition;
import com.search.domain.openeuler.dto.SortOpeneulerCondition;
import com.search.domain.openeuler.dto.TagsOpeneulerCondition;
import com.search.domain.openeuler.vo.OpenEulerVo;


public interface OpeneulerGateway {

    SuggResponceVo  getSuggByCondition(DocsOpeneulerCondition docsOpeneulerCondition);

    DocsResponceVo<OpenEulerVo> searchByCondition(DocsOpeneulerCondition docsOpeneulerCondition);


    CountResponceVo getSearchCountByCondition(DocsOpeneulerCondition docsOpeneulerCondition);


    SortResponceVo<OpenEulerVo> getSearchSortByCondition(SortOpeneulerCondition sortCondition);

    TagsResponceVo getSearchTagsByCondition(TagsOpeneulerCondition tagsCondition);

    SortResponceVo<OpenEulerVo> getDvideSearchSortByCondition(SortOpeneulerCondition sortCondition);


    SortResponceVo<OpenEulerVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
