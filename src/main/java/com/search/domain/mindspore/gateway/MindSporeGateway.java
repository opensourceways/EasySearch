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
    /**
     * Search for different types of data.
     *
     * @param searchDocsCondition The search condition for querying MindSpore.
     * @return ResponceResult.
     */
    DocsResponceVo<MindSporeVo> searchByCondition(DocsMindsporeCondition searchDocsCondition);

    /**
     * Search the number of   MindSpore data.
     *
     * @param searchDocsCondition The search condition for querying MindSpore.
     * @return CountResponceVo.
     */
    CountResponceVo getSearchCountByCondition(DocsMindsporeCondition searchDocsCondition);

    /**
     * Search for sort  of  MindSpore data.
     *
     * @param sortCondition The search condition for MindSpore.
     * @return SortResponceVo<MindSporeVo>.
     */
    SortResponceVo<MindSporeVo> getSearchSortByCondition(SortMindsporeCondition sortCondition);

    /**
     * Search the tags of   MindSpore data..
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition);

    /**
     * get Dvide Search Sort  of   MindSpore data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<MindSporeVo> getDvideSearchSortByCondition(SortMindsporeCondition sortCondition);

    /**
     * Search for  MindSpore document data
     *
     * @param DivideDocsBaseCondition The search condition for querying different types of data.
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<MindSporeVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
