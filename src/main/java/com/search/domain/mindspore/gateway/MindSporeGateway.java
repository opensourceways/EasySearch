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
import com.search.adapter.vo.SuggResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.mindspore.dto.DocsMindsporeCondition;
import com.search.domain.mindspore.dto.SuggMindsporeCondition;
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
     * Search the tags of   MindSpore data..
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition);

    /**
     * Implement search suggestions.
     *
     * @param searchSuggBaseCondition SuggBaseCondition.
     * @return SuggResponceVo.
     */
    SuggResponceVo getSuggByCondition(SuggMindsporeCondition searchSuggBaseCondition);
}
