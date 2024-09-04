package com.search.controller;

import com.search.adapter.SearchAdapter;
import com.search.adapter.condition.DocsCondition;
import com.search.adapter.condition.SortCondition;
import com.search.common.entity.ResponceResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/search/sort")
@RequiredArgsConstructor
public class
DivideController {
    private final SearchAdapter searchAdapter;

    @PostMapping("/{type}")
    public ResponceResult getDivideSortSearch(@PathVariable @NotBlank(message = "must have a type") String type, @RequestBody @NotEmpty(message = "Requires at least one condition") SortCondition condition) {

        return searchAdapter.getDivideSearch(condition, type);
    }


    @PostMapping("docs")
    public ResponceResult searchDivideDocsByCondition(@RequestBody @Validated DocsCondition condition) {

        return searchAdapter.getDocSearch(condition);
    }
}
