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

import com.search.adapter.condition.SortCondition;
import com.search.domain.base.dto.SearchSortBaseCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortOpeneulerCondition extends SearchSortBaseCondition {
    /**
     * 类别.
     */
    String category;
    /**
     * 标签.
     */
    String tags;
    /**
     * 档案.
     */
    String archives;

    /**
     * 有参构造，SortOpeneulerCondition
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件
     * @return SortOpeneulerCondition.
     */
    public SortOpeneulerCondition(String index, SortCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.type = condition.getType();
        this.category = condition.getCategory();
        this.tags = condition.getTags();
        this.archives = condition.getArchives();
    }
}
