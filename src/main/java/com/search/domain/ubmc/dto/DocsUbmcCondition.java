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
package com.search.domain.ubmc.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DocsUbmcCondition extends SearchDocsBaseCondition {
    /**
     *  filter list.
     */
    private List<UbmcFilter> filter;
    /**
     *  Limit list.
     */
    private List<UbmcLimit> limit;
    /**
     * order bt time.
     */
    private String orderTime;

    /**
     * 有参构造，初始化DocsUbmcCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public DocsUbmcCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        this.orderTime = condition.getOrderTime();
        setOpenmindFilter(condition);
        setOpenmindLimit(condition);
    }

    /**
     * 根据DocsCondition封装社区limit obj.
     *
     * @param condition 前台请求封装条件.
     */
    public void setOpenmindLimit(DocsCondition condition) {
        ArrayList<UbmcLimit> openmindLimits = new ArrayList<>();
        if (Objects.nonNull(condition.getLimit())) {
            condition.getLimit().stream().forEach(a -> {
                UbmcLimit openmindLimit = new UbmcLimit();
                openmindLimit.setType(a.getType());
                openmindLimit.setVersion(a.getVersion());
                openmindLimit.setDocsType(a.getDocsType());
                openmindLimit.setVersionTag(a.getVersionTag());
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
        ArrayList<UbmcFilter> openmindFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter())) {
            condition.getFilter().stream().forEach(a -> {
                UbmcFilter openmindFilter = new UbmcFilter();
                openmindFilter.setType(a.getType());
                openmindFilter.setVersion(a.getVersion());
                openmindFilter.setDocsType(a.getDocsType());
                openmindFilter.setVersionTag(a.getVersionTag());
                openmindFilters.add(openmindFilter);
            });
        }
        this.filter = openmindFilters;
    }

    /**
     * OpenmindFilter obj.
     */
    @Getter
    @Setter
    public static class UbmcFilter {
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
    public static class  UbmcLimit {
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
