package com.search.domain.base.dto;

import lombok.Data;

@Data
public class SearchTagsBaseCondition {
    protected String index;
    protected String category;
    protected String want;
    protected Object condition;
}
