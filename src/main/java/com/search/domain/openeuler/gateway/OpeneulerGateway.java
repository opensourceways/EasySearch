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
package com.search.domain.openeuler.gateway;

import com.search.adapter.vo.*;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.openeuler.dto.DocsOpeneulerCondition;
import com.search.domain.openeuler.dto.SortOpeneulerCondition;
import com.search.domain.openeuler.dto.TagsOpeneulerCondition;
import com.search.domain.openeuler.vo.OpenEulerVo;


public interface OpeneulerGateway {
    /**
     * Implement search suggestions.
     *
     * @param docsOpeneulerCondition The search condition for querying different types of data.
     * @return SuggResponceVo.
     */
    SuggResponceVo getSuggByCondition(DocsOpeneulerCondition docsOpeneulerCondition);

    /**
     * Search for different types of  Openeuler data.
     *
     * @param docsOpeneulerCondition The search condition for querying different types of data.
     * @return DocsResponceVo<OpenEulerVo> .
     */
    DocsResponceVo<OpenEulerVo> searchByCondition(DocsOpeneulerCondition docsOpeneulerCondition);

    /**
     * Search the number of   Openeuler data.
     *
     * @param docsOpeneulerCondition The search condition for Openeuler.
     * @return CountResponceVo.
     */
    CountResponceVo getSearchCountByCondition(DocsOpeneulerCondition docsOpeneulerCondition);

    /**
     * Search for sort  of  Openeuler data.
     *
     * @param sortCondition The search condition for Openeuler.
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenEulerVo> getSearchSortByCondition(SortOpeneulerCondition sortCondition);

    /**
     * Search the tags of   Openeuler data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    TagsResponceVo getSearchTagsByCondition(TagsOpeneulerCondition tagsCondition);

    /**
     * get Dvide Search Sort  of   Openeuler data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenEulerVo> getDvideSearchSortByCondition(SortOpeneulerCondition sortCondition);

    /**
     * Search for Euler document data
     *
     * @param DivideDocsBaseCondition The search condition for querying different types of data.
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenEulerVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
