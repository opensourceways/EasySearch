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
package com.search.domain.openmind.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DocsOpenmindCondition extends SearchDocsBaseCondition {
    /**
     * Openmind Limit list.
     */
    private List<OpenmindFilter> filter;

    /**
     * 有参构造，初始化DocsOpenmindCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public DocsOpenmindCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setOpenmindFilter(condition);
    }

    /**
     * 根据DocsCondition封装社区limit obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setOpenmindFilter(DocsCondition condition) {
        ArrayList<OpenmindFilter> openmindFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter())) {
            condition.getLimit().stream().forEach(a -> {
                OpenmindFilter filter = new OpenmindFilter();
                filter.setDocsType(a.getDocsType());
                openmindFilters.add(filter);
            });
        }
        this.filter = openmindFilters;
    }

    /**
     * OpenmindFilter obj.
     */
    @Getter
    @Setter
    private  static class OpenmindFilter {
        /**
         * 文档类型.
         */
        private String docsType;
    }
}
