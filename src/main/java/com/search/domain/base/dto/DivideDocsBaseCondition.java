package com.search.domain.base.dto;

import com.search.adapter.condition.DevideCondition;
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


    public DivideDocsBaseCondition(String index, String type, DevideCondition condition) {
        this.index = index;
        this.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        this.page = condition.getPage();
        this.pageSize = condition.getPageSize();
        this.keyword = condition.getKeyword();
        this.type = type;
        this.version = condition.getVersion();
    }


}
