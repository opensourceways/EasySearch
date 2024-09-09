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
import com.search.adapter.condition.DevideCondition;
import com.search.adapter.condition.SortCondition;
import com.search.common.entity.ResponceResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/sort")
@RequiredArgsConstructor
public class DivideController {
    /**
     * search adapter.
     */
    private final SearchAdapter searchAdapter;

    /**
     * 搜索特定type数据.
     *
     * @param condition condition obj.
     * @param type      类型 .
     * @return ResponceResult.
     */
    @PostMapping("/{type}")
    public ResponceResult getDivideSortSearch(@PathVariable @NotBlank(message = "must have a type") String type,
                                              @RequestBody @NotEmpty(message = "Requires at least one condition")
                                                      SortCondition condition) {

        return searchAdapter.getDivideSearch(condition, type);
    }

    /**
     * 搜索文档数据.
     *
     * @param condition condition obj.
     * @return ResponceResult.
     */
    @PostMapping("docs")
    public ResponceResult searchDivideDocsByCondition(@RequestBody @Validated DevideCondition condition) {

        return searchAdapter.getDocSearch(condition);
    }
}
