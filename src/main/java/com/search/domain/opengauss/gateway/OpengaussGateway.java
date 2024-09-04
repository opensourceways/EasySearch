package com.search.domain.opengauss.gateway;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.openeuler.vo.OpenEulerVo;
import com.search.domain.opengauss.dto.DocsOpengaussCondition;
import com.search.domain.opengauss.dto.SortOpengaussCondition;
import com.search.domain.opengauss.dto.TagsOpengaussCondition;
import com.search.domain.opengauss.vo.OpenGaussVo;


public interface OpengaussGateway {

    DocsResponceVo<OpenGaussVo> searchByCondition(DocsOpengaussCondition searchDocsCondition);

    CountResponceVo getSearchCountByCondition(DocsOpengaussCondition searchDocsCondition);

    SortResponceVo<OpenGaussVo> getSearchSortByCondition(SortOpengaussCondition sortCondition);

    TagsResponceVo getSearchTagsByCondition(TagsOpengaussCondition tagsCondition);

    SortResponceVo<OpenEulerVo> getDvideSearchSortByCondition(SortOpengaussCondition sortCondition);


    SortResponceVo<OpenEulerVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
