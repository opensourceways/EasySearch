package com.search.domain.base.dto;

import lombok.Data;

@Data
public class DivideDocsBaseCondition {
    private String keyword;
    private String version;
    protected String index;
    protected Integer pageFrom;
    protected Integer page;
    protected Integer pageSize;
    protected String type;
}
