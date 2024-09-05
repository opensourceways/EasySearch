package com.search.domain.mindspore.gateway;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.mindspore.dto.DocsMindsporeCondition;
import com.search.domain.mindspore.dto.SortMindsporeCondition;
import com.search.domain.mindspore.dto.TagsMindsporeCondition;
import com.search.domain.mindspore.vo.MindSporeVo;
public interface MindSporeGateway {

    DocsResponceVo<MindSporeVo> searchByCondition(DocsMindsporeCondition searchDocsCondition);

    CountResponceVo getSearchCountByCondition(DocsMindsporeCondition searchDocsCondition);


    SortResponceVo<MindSporeVo> getSearchSortByCondition(SortMindsporeCondition sortCondition);

    TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition);

    SortResponceVo<MindSporeVo> getDvideSearchSortByCondition(SortMindsporeCondition sortCondition);


    SortResponceVo<MindSporeVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
