package com.search.domain.base.dto;

import lombok.Data;

@Data
public class SearchDocsBaseCondition {
    protected String index;
    protected Integer pageFrom;
    protected Integer page;
    protected Integer pageSize;
    protected String keyword;
    protected String type;
    protected Object limit;
    protected Object filter;
}
