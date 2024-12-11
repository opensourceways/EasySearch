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
package com.search.domain.ubmc.gateway;

import com.search.adapter.vo.*;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.mindspore.dto.*;
import com.search.domain.ubmc.dto.DocsUbmcCondition;
import com.search.domain.ubmc.dto.SortOpenmindCondition;
import com.search.domain.ubmc.vo.UbmcVo;

public interface UbmcGateway {
    /**
     * Search for different types of data.
     *
     * @param searchDocsCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    DocsResponceVo<UbmcVo> searchByCondition(DocsUbmcCondition searchDocsCondition);

    /**
     * Search the number of data.
     *
     * @param searchDocsCondition The search condition of  Ubmc.
     * @return ResponceResult.
     */
    CountResponceVo getSearchCountByCondition(DocsUbmcCondition searchDocsCondition);

    /**
     * Search for sort  of  Ubmc data.
     *
     * @param sortCondition The search condition for  Ubmc.
     * @return SortResponceVo<UbmcVo>.
     */
    SortResponceVo<UbmcVo> getSearchSortByCondition(SortOpenmindCondition sortCondition);

    /**
     * Search the tags of    Ubmc data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    TagsResponceVo getSearchTagsByCondition(TagsUbmcCondition tagsCondition);

    /**
     * get Dvide Search Sort  of    Ubmc data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<UbmcVo>.
     */
    SortResponceVo<UbmcVo> getDvideSearchSortByCondition(SortOpenmindCondition sortCondition);

    /**
     * Search for Ubmc data.
     *
     * @param DivideDocsBaseCondition The search condition .
     * @return SortResponceVo<UbmcVo>.
     */
    SortResponceVo<UbmcVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition);


    /**
     * Implement search suggestions.
     *
     * @param suggUbmcCondition SuggBaseCondition.
     * @return SuggResponceVo.
     */
    SuggResponceVo getSuggByCondition(SuggUbmcCondition suggUbmcCondition);

    /**
     * Implement search hint.
     *
     * @param wordConditon wordConditon.
     * @return WordResponceVo.
     */
    WordResponceVo getWordByConditon(WordUbmcConditon wordConditon);
}
