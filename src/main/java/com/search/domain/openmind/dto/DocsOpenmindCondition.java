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
     * Openmind filter list.
     */
    private List<OpenmindFilter> filter;
    /**
     * Openmind Limit list.
     */
    private List<OpenmindLimit> limit;

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
        setOpenmindLimit(condition);
    }

    /**
     * 根据DocsCondition封装社区limit obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setOpenmindLimit(DocsCondition condition) {
        ArrayList<DocsOpenmindCondition.OpenmindLimit> openmindLimits = new ArrayList<>();
        if (Objects.nonNull(condition.getLimit())) {
            condition.getLimit().stream().forEach(a -> {
                OpenmindLimit openmindLimit = new OpenmindLimit();
                openmindLimit.setType(a.getType());
                openmindLimit.setVersion(a.getVersion());
                openmindLimits.add(openmindLimit);
            });
        }
        this.limit = openmindLimits;
    }


    /**
     * 根据DocsCondition封装社区limit obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setOpenmindFilter(DocsCondition condition) {
        ArrayList<OpenmindFilter> openmindFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter())) {
            condition.getFilter().stream().forEach(a -> {
                OpenmindFilter filter = new OpenmindFilter();
                filter.setType(a.getType());
                filter.setVersion(a.getVersion());
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
    public static class OpenmindFilter {
        /**
         * 数据类型.
         */
        private String type;
        /**
         * 文档版本.
         */
        private String version;
        /**
         * 版本标签.
         */
        private String versionTag;

        /**
         * 文档类型.
         */
        private String docsType;
    }

    /**
     * OpenmindLimit obj.
     */
    @Getter
    @Setter
    public static class OpenmindLimit {
        /**
         * 文档类型.
         */
        private String type;
        /**
         * 文档版本.
         */
        private String version;

        /**
         * 版本标签.
         */
        private String versionTag;

        /**
         * 文档类型.
         */
        private String docsType;
    }
}
