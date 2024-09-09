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
package com.search.domain.openeuler.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DocsOpeneulerCondition extends SearchDocsBaseCondition {
    /**
     * Openeuler Limit list.
     */
    private List<OpeneulerLimit> limit;
    /**
     * Openeuler filter list.
     */
    private List<OpeneulerFilter> filter;

    /**
     * 有参构造，初始化DocsOpeneulerCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件
     */
    public DocsOpeneulerCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setOpeneulerLimit(condition);
        setOpeneulerFilter(condition);
    }

    /**
     * 根据DocsCondition封装社区limit obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setOpeneulerLimit(DocsCondition condition) {
        ArrayList<OpeneulerLimit> openeulerLimits = new ArrayList<>();
        if (!Objects.isNull(condition.getLimit())) {
            condition.getLimit().stream().forEach(a -> {
                OpeneulerLimit openeulerLimit = new OpeneulerLimit();
                openeulerLimit.setType(a.getType());
                openeulerLimit.setVersion(a.getVersion());
                openeulerLimits.add(openeulerLimit);
            });
        }
        this.limit = openeulerLimits;
    }

    /**
     * 根据DocsCondition封装社区filter obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setOpeneulerFilter(DocsCondition condition) {
        ArrayList<OpeneulerFilter> openeulerFilters = new ArrayList<>();
        if (!Objects.isNull(condition.getFilter())) {
            condition.getLimit().stream().forEach(a -> {
                OpeneulerFilter openeulerFilter = new OpeneulerFilter();
                openeulerFilter.setType(a.getType());
                openeulerFilter.setVersion(a.getVersion());
                openeulerFilters.add(openeulerFilter);
            });
        }
        this.filter = openeulerFilters;
    }

    /**
     * OpeneulerLimit obj.
     */
    @Getter
    @Setter
    private static class OpeneulerLimit {
        /**
         * 类型.
         */
        private String type;
        /**
         * 版本.
         */
        private String version;
    }

    /**
     * OpeneulerFilter obj.
     */
    @Getter
    @Setter
    private static class OpeneulerFilter {
        /**
         * 类型.
         */
        private String type;
        /**
         * 版本.
         */
        private String version;
    }
}
