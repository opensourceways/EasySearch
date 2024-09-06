package com.search.domain.base.vo;

import lombok.Data;

@Data
public class CountVo {
    protected String key;
    protected Long doc_count;
}
