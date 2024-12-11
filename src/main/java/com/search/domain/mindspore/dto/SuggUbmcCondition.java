package com.search.domain.mindspore.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchSuggBaseCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SuggUbmcCondition extends SearchSuggBaseCondition {
    /**
     * 有参构造，初始化SuggUbmcCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public SuggUbmcCondition(DocsCondition condition, String index) {
        super.keyword = condition.getKeyword();
        super.index = index;
    }

    /**
     * 无参构造，初始化SuggUbmcCondition.
     */
    public SuggUbmcCondition() {

    }
}
