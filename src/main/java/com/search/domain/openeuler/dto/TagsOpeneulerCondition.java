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

import com.search.adapter.condition.TagsCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagsOpeneulerCondition extends SearchTagsBaseCondition {
    /**
     * 有参构造，TagsOpeneulerCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public TagsOpeneulerCondition(TagsCondition condition, String index) {
        super.category = condition.getCategory();
        super.index = index;
        super.want = condition.getWant();
        super.condition = condition.getCondition();
    }
}
