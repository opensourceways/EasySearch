package com.search.domain.base.dto;

import lombok.Data;

@Data
public class DivideSortBaseCondition {
    protected String keyword;
    protected String index;
    protected Integer pageFrom;
    protected Integer page;
    protected Integer pageSize;
}
