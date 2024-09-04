package com.search.adapter;

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
import com.search.domain.openmind.dto.SortOpenmindCondition;
import com.search.domain.openmind.dto.TagsOpenmindCondition;
import com.search.domain.openmind.gateway.OpenmindGateway;
import com.search.domain.softcenter.dto.DocsSoftcenterCondition;
import com.search.domain.softcenter.gateway.SoftcenterGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchAdapter {
    private final OpeneulerGateway openeulerGateway;
    private final MindSporeGateway mindSporeGateway;
    private final OpengaussGateway opengaussGateway;
    private final OpenmindGateway openmindGateway;
    private final SoftcenterGateway softcenterGateway;


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


    public ResponceResult geSortByCondition(SortCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                SortOpeneulerCondition sortOpeneulerCondition = new SortOpeneulerCondition();
                return ResponceResult.ok(openeulerGateway.getSearchSortByCondition(sortOpeneulerCondition));

            case SourceConstant.SOURCE_MINDSPORE:
                SortMindsporeCondition sortMindsporeCondition = new SortMindsporeCondition();
                return ResponceResult.ok(mindSporeGateway.getSearchSortByCondition(sortMindsporeCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                SortOpengaussCondition sortOpengaussCondition = new SortOpengaussCondition();
                return ResponceResult.ok(opengaussGateway.getSearchSortByCondition(sortOpengaussCondition));

            case SourceConstant.SOURCE_OPENMIND:
                SortOpenmindCondition sortOpenmindCondition = new SortOpenmindCondition();
                return ResponceResult.ok(openmindGateway.getSearchSortByCondition(sortOpenmindCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }


    public ResponceResult getTagsByCondition(TagsCondition condition) {
        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                TagsOpeneulerCondition tagsOpeneulerCondition = new TagsOpeneulerCondition();
                return ResponceResult.ok(openeulerGateway.getSearchTagsByCondition(tagsOpeneulerCondition));

            case SourceConstant.SOURCE_MINDSPORE:
                TagsMindsporeCondition tagsMindsporeCondition = new TagsMindsporeCondition();
                return ResponceResult.ok(mindSporeGateway.getSearchTagsByCondition(tagsMindsporeCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                TagsOpengaussCondition tagsOpengaussCondition = new TagsOpengaussCondition();
                return ResponceResult.ok(opengaussGateway.getSearchTagsByCondition(tagsOpengaussCondition));

            case SourceConstant.SOURCE_OPENMIND:
                TagsOpenmindCondition tagsOpenmindCondition = new TagsOpenmindCondition();
                return ResponceResult.ok(openmindGateway.getSearchTagsByCondition(tagsOpenmindCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }


    public ResponceResult getDivideSearch(SortCondition condition, String type) {

        String dataSource = ThreadLocalCache.getDataSource();
        condition.setType(type);
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                SortOpeneulerCondition sortOpeneulerCondition = new SortOpeneulerCondition();
                return ResponceResult.ok(openeulerGateway.getDvideSearchSortByCondition(sortOpeneulerCondition));

            case SourceConstant.SOURCE_MINDSPORE:
                SortMindsporeCondition sortMindsporeCondition = new SortMindsporeCondition();
                return ResponceResult.ok(mindSporeGateway.getDvideSearchSortByCondition(sortMindsporeCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                SortOpengaussCondition sortOpengaussCondition = new SortOpengaussCondition();
                return ResponceResult.ok(opengaussGateway.getDvideSearchSortByCondition(sortOpengaussCondition));

            case SourceConstant.SOURCE_OPENMIND:
                SortOpenmindCondition sortOpenmindCondition = new SortOpenmindCondition();
                return ResponceResult.ok(openmindGateway.getDvideSearchSortByCondition(sortOpenmindCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }


    public ResponceResult getDocSearch(DocsCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        DivideDocsBaseCondition divideDocsBaseCondition = new DivideDocsBaseCondition();
        switch (dataSource) {
            case SourceConstant.SOURCE_OPENEULER:
                return ResponceResult.ok(openeulerGateway.searchDocByType(divideDocsBaseCondition));

            case SourceConstant.SOURCE_MINDSPORE:
                return ResponceResult.ok(mindSporeGateway.searchDocByType(divideDocsBaseCondition));

            case SourceConstant.SOURCE_OPENGAUSS:
                return ResponceResult.ok(opengaussGateway.searchDocByType(divideDocsBaseCondition));

            case SourceConstant.SOURCE_OPENMIND:
                return ResponceResult.ok(openmindGateway.searchDocByType(divideDocsBaseCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }

    }


    public ResponceResult getDocAllByCondition(DocsCondition condition) {

        String dataSource = ThreadLocalCache.getDataSource();
        String index = dataSource + SearchConstant.INDEX_CONNECT + condition.getLang();
        switch (dataSource) {
            case SourceConstant.SOURCE_SOFTCENTER:
                DocsSoftcenterCondition docsSoftcenterCondition = new DocsSoftcenterCondition();
                return ResponceResult.ok(softcenterGateway.getSearchAllByCondition(docsSoftcenterCondition));

            default:
                return ResponceResult.fail("not supported currently source", null);
        }
    }


}
