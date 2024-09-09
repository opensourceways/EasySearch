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
package com.search.controller;

import com.search.adapter.SearchAdapter;
import com.search.adapter.condition.DocsCondition;
import com.search.adapter.condition.SortCondition;
import com.search.adapter.condition.TagsCondition;
import com.search.common.entity.ResponceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    /**
     * search adapter.
     */
    private final SearchAdapter searchAdapter;

    /**
     * 搜索符合条件的各种类型数据.
     *
     * @param condition controller conditon.
     * @return ResponceResult.
     */
    @PostMapping("docs")
    public ResponceResult searchDataByCondition(@RequestBody @Validated DocsCondition condition) {
        return searchAdapter.searchDataByCondition(condition);
    }

    /**
     * 搜索符合条件的各种类型数据的数量.
     *
     * @param condition controller conditon.
     * @return ResponceResult.
     */
    @PostMapping("count")
    public ResponceResult getCountByCondition(@RequestBody @Validated DocsCondition condition) {
        return searchAdapter.getDocsCountByCondition(condition);
    }

    /**
     * 搜索符合特定类型的数据.
     *
     * @param condition controller conditon.
     * @return ResponceResult.
     */
    @PostMapping("sort")
    public ResponceResult getSort(@RequestBody @Validated SortCondition condition) {
        return searchAdapter.geSortByCondition(condition);
    }

    /**
     * 实现搜索数据的标签.
     *
     * @param searchTags controller conditon.
     * @return ResponceResult.
     */
    @PostMapping("tags")
    public ResponceResult getTags(@RequestBody @Validated TagsCondition searchTags) {
        return searchAdapter.getTagsByCondition(searchTags);
    }

    /**
     * 搜索符合条件数据的特定字段以及数量.
     *
     * @param condition controller conditon.
     * @return ResponceResult.
     */
    @PostMapping("docsAll")
    public ResponceResult searchAllByKeyword(@RequestBody @Validated DocsCondition condition) {
        return searchAdapter.getDocAllByCondition(condition);

    }

    /**
     * 搜索建议.
     *
     * @param condition controller conditon.
     * @return ResponceResult.
     */
    @PostMapping("/sugg")
    public ResponceResult getSugg(@RequestBody @Validated DocsCondition condition) {
        return searchAdapter.getSuggByCondition(condition);
    }
}
