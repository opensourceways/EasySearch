package com.search.domain.mindspore.dto;

import com.search.adapter.condition.TagsCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;

public class TagsUbmcCondition extends SearchTagsBaseCondition {
    /**
     * 有参构造，初始化TagsMindsporeCondition.
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件.
     */
    public TagsUbmcCondition(TagsCondition condition, String index) {
        super.category = condition.getCategory();
        super.index = index;
        super.want = condition.getWant();
        super.condition = condition.getCondition();
    }
}
