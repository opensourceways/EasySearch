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
    private final SearchAdapter searchAdapter;

    @PostMapping("docs")
    public ResponceResult searchDataByCondition(@RequestBody @Validated DocsCondition condition) {
        System.out.println("开始请求");
        return searchAdapter.searchDataByCondition(condition);
    }


    @PostMapping("count")
    public ResponceResult getCountByCondition(@RequestBody @Validated DocsCondition condition) {
        return searchAdapter.getDocsCountByCondition(condition);
    }


    @PostMapping("sort")
    public ResponceResult getSort(@RequestBody @Validated SortCondition condition) {
        return searchAdapter.geSortByCondition(condition);
    }


    @PostMapping("tags")
    public ResponceResult getTags(@RequestBody @Validated TagsCondition searchTags) {
        return searchAdapter.getTagsByCondition(searchTags);
    }


    @PostMapping("docsAll")
    public ResponceResult searchAllByKeyword(@RequestBody @Validated DocsCondition condition) {
        return searchAdapter.getDocAllByCondition(condition);

    }
}
