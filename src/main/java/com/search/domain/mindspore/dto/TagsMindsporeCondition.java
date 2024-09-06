package com.search.domain.mindspore.dto;

import com.search.adapter.condition.TagsCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;

public class TagsMindsporeCondition extends SearchTagsBaseCondition {
    public TagsMindsporeCondition(TagsCondition condition, String index) {
        super.category = condition.getCategory();
        super.index = index;
        super.want = condition.getWant();
        super.condition = condition.getCondition();
    }
}
