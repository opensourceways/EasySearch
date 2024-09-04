package com.search.adapter.condition;

import lombok.Data;


@Data
public class SortCondition {
    Integer page;
    Integer pageSize;
    Integer keyword;
    String type;
    Boolean isNeedHighlight;
    String lang;
}
