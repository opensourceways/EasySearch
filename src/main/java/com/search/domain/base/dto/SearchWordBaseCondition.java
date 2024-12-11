package com.search.domain.base.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchWordBaseCondition {
    /**
     * 输入语言.
     */
    protected String query;

    /**
     * 数据索引.
     */
    protected String index;
}
