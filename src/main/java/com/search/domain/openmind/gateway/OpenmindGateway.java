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
    /**
     * Search for different types of data.
     *
     * @param searchDocsCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    DocsResponceVo<OpenMindVo> searchByCondition(DocsOpenmindCondition searchDocsCondition);

    /**
     * Search the number of data.
     *
     * @param searchDocsCondition The search condition of openmind.
     * @return ResponceResult.
     */
    CountResponceVo getSearchCountByCondition(DocsOpenmindCondition searchDocsCondition);

    /**
     * Search for sort  of  Openmind data.
     *
     * @param sortCondition The search condition for Openmind.
     * @return SortResponceVo<OpenMindVo>.
     */
    SortResponceVo<OpenMindVo> getSearchSortByCondition(SortOpenmindCondition sortCondition);

    /**
     * Search the tags of   Openmind data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    TagsResponceVo getSearchTagsByCondition(TagsOpenmindCondition tagsCondition);

    /**
     * get Dvide Search Sort  of   Openmind data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenMindVo> getDvideSearchSortByCondition(SortOpenmindCondition sortCondition);

    /**
     * Search for  Openmind document data
     *
     * @param DivideDocsBaseCondition The search condition .
     * @return SortResponceVo<OpenMindVo>.
     */
    SortResponceVo<OpenMindVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
