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
import com.search.domain.base.dto.SearchSuggBaseCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SuggMindsporeCondition extends SearchSuggBaseCondition {
    /**
     * 有参构造，初始化SuggMindsporeCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public SuggMindsporeCondition(DocsCondition condition, String index) {
        super.keyword = condition.getKeyword();
        super.index = index;
    }
    /**
     * 无参构造，初始化SuggMindsporeCondition.
     */
    public SuggMindsporeCondition() {

    }
}
