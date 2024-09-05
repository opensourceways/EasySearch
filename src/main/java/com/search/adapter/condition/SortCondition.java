package com.search.adapter.condition;

import lombok.Data;


@Data
public class SortCondition {
    Integer page;
    Integer pageSize;
    String category;
    String tags;
    String archives;
    String lang;
    String type;
    String keyword;
}
