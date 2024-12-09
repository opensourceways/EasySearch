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
package com.search.infrastructure.search.ubmc;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SortResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.ubmc.dto.DocsUbmcCondition;
import com.search.domain.ubmc.dto.SortOpenmindCondition;
import com.search.domain.ubmc.dto.TagsOpenmindCondition;
import com.search.domain.ubmc.gateway.UbmcGateway;
import com.search.domain.ubmc.vo.UbmcVo;
import com.search.infrastructure.search.ubmc.dataobject.UbmcDo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class UbmcGatewayImpl extends BaseFounctionGateway implements UbmcGateway {

    /**
     * Search for different types of data.
     *
     * @param searchDocsCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    @Override
    public DocsResponceVo<UbmcVo> searchByCondition(DocsUbmcCondition searchDocsCondition) {
        SearchRequest defaultSearchRequest = requestBuilder.getDefaultDocsSearchRequest(searchDocsCondition);
        SearchResponse searchResponse = executeDefaultEsSearch(defaultSearchRequest);
        List<Map<String, Object>> dateMapList = responceHandler.getDefaultsHightResponceToMapList(
                searchResponse, Arrays.asList("title"), "textContent");
        List<UbmcDo> ubmcDos = CommonConverter.toDoList(dateMapList, UbmcDo.class);
        List<UbmcVo> ubmcVos = CommonConverter.toBaseVoList(ubmcDos, UbmcVo.class);
        // 默认v0.0.0版本显示为“”
        for (UbmcVo ubmcVo : ubmcVos) {
            if ("v0.0.0".equals(ubmcVo.getVersion())) {
                ubmcVo.setVersion("");
            }
        }
        DocsResponceVo docsResponceVo = new DocsResponceVo(ubmcVos,
                searchDocsCondition.getPageSize(),
                searchDocsCondition.getPage(),
                searchDocsCondition.getKeyword());
        docsResponceVo.setCount(searchResponse.getHits().getTotalHits().value);
        return docsResponceVo;
    }

    /**
     * Search the number of data.
     *
     * @param searchDocsCondition The search condition of ubmc.
     * @return ResponceResult.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsUbmcCondition searchDocsCondition) {
        return super.getDefaultSearchCountByCondition(searchDocsCondition);
    }

    /**
     * Search for sort  of  Ubmc data.
     *
     * @param sortCondition The search condition for  Ubmc.
     * @return SortResponceVo<UbmcVo>.
     */
    @Override
    public SortResponceVo<UbmcVo> getSearchSortByCondition(SortOpenmindCondition sortCondition) {
        return null;
    }

    /**
     * Search the tags of    Ubmc data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsOpenmindCondition tagsCondition) {
        return null;
    }
    /**
     * get Dvide Search Sort  of    Ubmc data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<UbmcVo>.
     */
    @Override
    public SortResponceVo<UbmcVo> getDvideSearchSortByCondition(SortOpenmindCondition sortCondition) {
        return null;
    }
    /**
     * Search for Ubmc data.
     *
     * @param DivideDocsBaseCondition The search condition .
     * @return SortResponceVo<UbmcVo>.
     */
    @Override
    public SortResponceVo<UbmcVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition) {
        return null;
    }
}
