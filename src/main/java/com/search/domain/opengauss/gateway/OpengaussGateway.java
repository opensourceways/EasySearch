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
package com.search.domain.opengauss.gateway;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.opengauss.dto.DocsOpengaussCondition;
import com.search.domain.opengauss.dto.SortOpengaussCondition;
import com.search.domain.opengauss.dto.TagsOpengaussCondition;
import com.search.domain.opengauss.vo.OpenGaussVo;


public interface OpengaussGateway {
    /**
     * Search for different types of data.
     *
     * @param searchDocsCondition The search condition for querying different types of data.
     * @return DocsResponceVo<OpenGaussVo>.
     */
    DocsResponceVo<OpenGaussVo> searchByCondition(DocsOpengaussCondition searchDocsCondition);

    /**
     * Search the number of data.
     *
     * @param searchDocsCondition The search condition for querying different types of data.
     * @return CountResponceVo.
     */
    CountResponceVo getSearchCountByCondition(DocsOpengaussCondition searchDocsCondition);

    /**
     * Search for sort  of  Opengauss data.
     *
     * @param sortCondition The search condition for Openeuler.
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenGaussVo> getSearchSortByCondition(SortOpengaussCondition sortCondition);

    /**
     * Search the tags of   Opengauss data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    TagsResponceVo getSearchTagsByCondition(TagsOpengaussCondition tagsCondition);

    /**
     * get Dvide Search Sort  of   Opengauss data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenGaussVo> getDvideSearchSortByCondition(SortOpengaussCondition sortCondition);

    /**
     * Search for  Opengauss document data.
     *
     * @param DivideDocsBaseCondition The search condition for querying different types of data.
     * @return SortResponceVo<OpenEulerVo>.
     */
    SortResponceVo<OpenGaussVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);
}
