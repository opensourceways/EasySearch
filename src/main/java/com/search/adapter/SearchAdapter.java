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
package com.search.adapter;

import com.search.adapter.condition.DevideCondition;
import com.search.adapter.condition.DocsCondition;
import com.search.adapter.condition.TagsCondition;
import com.search.adapter.condition.SortCondition;
import com.search.common.constant.SearchConstant;
import com.search.common.constant.SourceConstant;
import com.search.common.entity.ResponceResult;
import com.search.common.thread.ThreadLocalCache;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.mindspore.dto.DocsMindsporeCondition;
import com.search.domain.mindspore.dto.SortMindsporeCondition;
import com.search.domain.mindspore.dto.TagsMindsporeCondition;
import com.search.domain.mindspore.gateway.MindSporeGateway;
import com.search.domain.openeuler.dto.DocsOpeneulerCondition;
import com.search.domain.openeuler.dto.SortOpeneulerCondition;
import com.search.domain.openeuler.dto.TagsOpeneulerCondition;
import com.search.domain.openeuler.gateway.OpeneulerGateway;
import com.search.domain.opengauss.dto.DocsOpengaussCondition;
import com.search.domain.opengauss.dto.SortOpengaussCondition;
import com.search.domain.opengauss.dto.TagsOpengaussCondition;
import com.search.domain.opengauss.gateway.OpengaussGateway;
import com.search.domain.openmind.dto.DocsOpenmindCondition;
import com.search.domain.openmind.gateway.OpenmindGateway;
import com.search.domain.softcenter.dto.DocsSoftcenterCondition;
import com.search.domain.softcenter.gateway.SoftcenterGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchAdapter {
    /**
     * Provide openeuler search capability.
     */
    private final OpeneulerGateway openeulerGateway;
    /**
     * Provide  mindSpore search capability.
     */
    private final MindSporeGateway mindSporeGateway;
    /**
     * Provide pengauss search capability.
     */
    private final OpengaussGateway opengaussGateway;
    /**
     * Provide openmind search capability.
     */
    private final OpenmindGateway openmindGateway;
    /**
     * Provide softcenter search capability.
     */
    private final SoftcenterGateway softcenterGateway;

    /**
     * 根据数据源适配gateway以实现搜索符合条件的各种类型数据.
     *
     * @param condition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    public ResponceResult searchDataByCondition(DocsCondition condition) {
        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                DocsOpeneulerCondition docsOpeneulerCondition = new DocsOpeneulerCondition(index, condition);
                return ResponceResult.ok(openeulerGateway.searchByCondition(docsOpeneulerCondition));
            case SourceConstant.SOURCE_MINDSPORE:
                DocsMindsporeCondition docsMindsporeCondition = new DocsMindsporeCondition(index, condition);
                return ResponceResult.ok(mindSporeGateway.searchByCondition(docsMindsporeCondition));
            case SourceConstant.SOURCE_OPENGAUSS:
                DocsOpengaussCondition docsOpengaussCondition = new DocsOpengaussCondition(index, condition);
                return ResponceResult.ok(opengaussGateway.searchByCondition(docsOpengaussCondition));
            case SourceConstant.SOURCE_OPENMIND:
                DocsOpenmindCondition docsOpenmindCondition = new DocsOpenmindCondition(index, condition);
                return ResponceResult.ok(openmindGateway.searchByCondition(docsOpenmindCondition));

            case SourceConstant.SOURCE_SOFTCENTER:
                DocsSoftcenterCondition docsSoftcenterCondition = new DocsSoftcenterCondition();
                return ResponceResult.ok(softcenterGateway.searchByCondition(docsSoftcenterCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }


    }

    /**
     * 根据数据源适配gateway以实现搜索符合条件的各种类型数据的数量.
     *
     * @param condition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    public ResponceResult getDocsCountByCondition(DocsCondition condition) {
        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {

            case SourceConstant.SOURCE_OPENEULER:
                DocsOpeneulerCondition docsOpeneulerCondition = new DocsOpeneulerCondition(index, condition);
                return ResponceResult.ok(openeulerGateway.getSearchCountByCondition(docsOpeneulerCondition));

            case SourceConstant.SOURCE_MINDSPORE:
                DocsMindsporeCondition docsMindsporeCondition = new DocsMindsporeCondition(index, condition);
                return ResponceResult.ok(mindSporeGateway.getSearchCountByCondition(docsMindsporeCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                DocsOpengaussCondition docsOpengaussCondition = new DocsOpengaussCondition(index, condition);
                return ResponceResult.ok(opengaussGateway.getSearchCountByCondition(docsOpengaussCondition));

            case SourceConstant.SOURCE_OPENMIND:
                DocsOpenmindCondition docsOpenmindCondition = new DocsOpenmindCondition(index, condition);
                return ResponceResult.ok(openmindGateway.getSearchCountByCondition(docsOpenmindCondition));

            case SourceConstant.SOURCE_SOFTCENTER:
                DocsSoftcenterCondition docsSoftcenterCondition = new DocsSoftcenterCondition();
                return ResponceResult.ok(softcenterGateway.getSearchCountByCondition(docsSoftcenterCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }

    }

    /**
     * 根据数据源适配gateway以实现搜索符合特定类型的数据.
     *
     * @param condition The search condition for querying Sort.
     * @return ResponceResult.
     */
    public ResponceResult geSortByCondition(SortCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                SortOpeneulerCondition sortOpeneulerCondition = new SortOpeneulerCondition(index, condition);
                return ResponceResult.ok(openeulerGateway.getSearchSortByCondition(sortOpeneulerCondition));


            case SourceConstant.SOURCE_OPENGAUSS:
                SortOpengaussCondition sortOpengaussCondition = new SortOpengaussCondition();
                return ResponceResult.ok(opengaussGateway.getSearchSortByCondition(sortOpengaussCondition));


            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }

    /**
     * 根据数据源适配gateway以实现搜索数据的标签.
     *
     * @param condition The search condition for querying tags.
     * @return ResponceResult.
     */
    public ResponceResult getTagsByCondition(TagsCondition condition) {
        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                TagsOpeneulerCondition tagsOpeneulerCondition = new TagsOpeneulerCondition(condition, index);
                return ResponceResult.ok(openeulerGateway.getSearchTagsByCondition(tagsOpeneulerCondition));

            case SourceConstant.SOURCE_MINDSPORE:
                TagsMindsporeCondition tagsMindsporeCondition = new TagsMindsporeCondition(condition, index);
                return ResponceResult.ok(mindSporeGateway.getSearchTagsByCondition(tagsMindsporeCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                TagsOpengaussCondition tagsOpengaussCondition = new TagsOpengaussCondition(condition, index);
                return ResponceResult.ok(opengaussGateway.getSearchTagsByCondition(tagsOpengaussCondition));


            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }

    /**
     * 根据数据源适配gateway以实现搜索特定type数据.
     *
     * @param condition The search condition for querying different types of data.
     * @param type      data type.
     * @return ResponceResult.
     */
    public ResponceResult getDivideSearch(SortCondition condition, String type) {

        String dataSource = ThreadLocalCache.getDataSource();
        condition.setType(type);
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                SortOpeneulerCondition sortOpeneulerCondition = new SortOpeneulerCondition(index, condition);
                return ResponceResult.ok(openeulerGateway.getDvideSearchSortByCondition(sortOpeneulerCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                SortOpengaussCondition sortOpengaussCondition = new SortOpengaussCondition();
                return ResponceResult.ok(opengaussGateway.getDvideSearchSortByCondition(sortOpengaussCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }

    /**
     * 根据数据源适配gateway以实现搜索文档数据.
     *
     * @param condition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    public ResponceResult getDocSearch(DevideCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        DivideDocsBaseCondition divideDocsBaseCondition = new DivideDocsBaseCondition(index, "docs", condition);
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                return ResponceResult.ok(openeulerGateway.searchDocByType(divideDocsBaseCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                return ResponceResult.ok(opengaussGateway.searchDocByType(divideDocsBaseCondition));

            case SourceConstant.SOURCE_OPENMIND:
                return ResponceResult.ok(openmindGateway.searchDocByType(divideDocsBaseCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }

    }

    /**
     * 根据数据源适配gateway以实现搜索符合条件数据的特定字段以及数量.
     *
     * @param condition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    public ResponceResult getDocAllByCondition(DocsCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        //  String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_SOFTCENTER:
                DocsSoftcenterCondition docsSoftcenterCondition = new DocsSoftcenterCondition();
                return ResponceResult.ok(softcenterGateway.getSearchAllByCondition(docsSoftcenterCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }

    /**
     * 根据数据源适配gateway以实现搜索建议.
     *
     * @param condition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    public ResponceResult getSuggByCondition(DocsCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                DocsOpeneulerCondition docsOpeneulerCondition = new DocsOpeneulerCondition(index, condition);
                return ResponceResult.ok(openeulerGateway.getSuggByCondition(docsOpeneulerCondition));
            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }
}
