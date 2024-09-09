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
package com.search.domain.mindspore.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DocsMindsporeCondition extends SearchDocsBaseCondition {
    /**
     * Mindspore Limit list.
     */
    private List<MindsporeLimit> limit;

    /**
     * Mindspore filter list.
     */
    private List<MindsporeFilter> filter;

    /**
     * 有参构造，初始化DocsMindsporeCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public DocsMindsporeCondition(String index, DocsCondition condition) {
        this.index = index;
        this.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        this.page = condition.getPage();
        this.pageSize = condition.getPageSize();
        this.keyword = condition.getKeyword();
        this.type = condition.getType();
        setMindsporeLimit(condition);
        setMindsporeFilter(condition);
    }

    /**
     * 根据DocsCondition封装社区limit obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setMindsporeLimit(DocsCondition condition) {
        ArrayList<MindsporeLimit> mindsporeLimits = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter())) {
            condition.getLimit().stream().forEach(a -> {
                MindsporeLimit mindsporeLimit = new MindsporeLimit();
                mindsporeLimit.setComponents(a.getComponents());
                mindsporeLimit.setVersion(a.getVersion());
                mindsporeLimit.setName(a.getName());
                mindsporeLimits.add(mindsporeLimit);
            });
        }
        this.limit = mindsporeLimits;
    }

    /**
     * 根据DocsCondition封装社区filter obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setMindsporeFilter(DocsCondition condition) {
        ArrayList<MindsporeFilter> mindsporeFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter())) {
            condition.getLimit().stream().forEach(a -> {
                MindsporeFilter mindsporeFilter = new MindsporeFilter();
                mindsporeFilter.setComponents(a.getComponents());
                mindsporeFilter.setVersion(a.getVersion());
                mindsporeFilter.setName(a.getName());
                mindsporeFilters.add(mindsporeFilter);
            });
        }

        this.filter = mindsporeFilters;
    }

    /**
     * MindsporeLimit obj.
     */
    @Getter
    @Setter
    public static  class MindsporeLimit {
        /**
         * 内容.
         */
        private String components;
        /**
         * 版本.
         */
        private String version;
        /**
         * 名称.
         */
        private String name;
    }

    /**
     * MindsporeFilter obj.
     */
    @Getter
    @Setter
    public static class MindsporeFilter {
        /**
         * 内容.
         */
        private String components;
        /**
         * 版本.
         */
        private String version;
        /**
         * 名称.
         */
        private String name;
    }
}
