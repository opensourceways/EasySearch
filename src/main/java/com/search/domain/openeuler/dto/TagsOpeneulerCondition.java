package com.search.domain.openeuler.dto;

import com.search.adapter.condition.TagsCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;

public class TagsOpeneulerCondition extends SearchTagsBaseCondition {
    public TagsOpeneulerCondition(TagsCondition condition, String index) {
        super.category = condition.getCategory();
        super.index = index;
        super.want = condition.getWant();
        super.condition=condition.getCondition();
    }
}
