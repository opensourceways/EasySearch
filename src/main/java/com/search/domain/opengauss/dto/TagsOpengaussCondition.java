package com.search.domain.opengauss.dto;

import com.search.adapter.condition.TagsCondition;
import com.search.domain.base.dto.SearchTagsBaseCondition;


public class TagsOpengaussCondition extends SearchTagsBaseCondition {
    public TagsOpengaussCondition(TagsCondition condition, String index) {
        super.category = condition.getCategory();
        super.index = index;
        super.want = condition.getWant();
        super.condition = condition.getCondition();
    }

    public TagsOpengaussCondition() {

    }
}
