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
package com.search.infrastructure.search.mindspore;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SuggResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.mindspore.dto.DocsMindsporeCondition;
import com.search.domain.mindspore.dto.SuggMindsporeCondition;
import com.search.domain.mindspore.dto.TagsMindsporeCondition;
import com.search.domain.mindspore.gateway.MindSporeGateway;
import com.search.domain.mindspore.vo.MindSporeVo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.mindspore.dataobject.MindsporeDo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class MindsporeGatewayImpl extends BaseFounctionGateway implements MindSporeGateway {
    /**
     * Search for different types of data.
     *
     * @param searchBaseCondition The search condition for querying MindSpore.
     * @return ResponceResult.
     */
    @Override
    public DocsResponceVo<MindSporeVo> searchByCondition(DocsMindsporeCondition searchBaseCondition) {
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchBaseCondition);
        List<MindsporeDo> mindsporeDos = CommonConverter.toDoList(dateMapList, MindsporeDo.class);
        List<MindSporeVo> mindSporeVos = CommonConverter.toBaseVoList(mindsporeDos, MindSporeVo.class);
        DocsResponceVo<MindSporeVo> docsResponceVo = new DocsResponceVo(mindSporeVos,
                searchBaseCondition.getPageSize(),
                searchBaseCondition.getPage(),
                searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    /**
     * Search the number of   MindSpore data.
     *
     * @param condition The search condition for querying MindSpore.
     * @return CountResponceVo.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsMindsporeCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }


    /**
     * Search the tags of   MindSpore data..
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    /**
     * Implement search suggestions.
     *
     * @param suggMindsporeCondition The search condition for querying different types of data.
     * @return SuggResponceVo.
     */
    @Override
    public SuggResponceVo getSuggByCondition(SuggMindsporeCondition suggMindsporeCondition) {
        suggMindsporeCondition.setAnalyzer("ik_smart");
        suggMindsporeCondition.setFieldname("title");
        suggMindsporeCondition.setMinWordLength(2);
        suggMindsporeCondition.setPrefixLength(0);
        suggMindsporeCondition.setSize(3);
        return super.getDefaultSuggByCondition(suggMindsporeCondition);
    }

}
