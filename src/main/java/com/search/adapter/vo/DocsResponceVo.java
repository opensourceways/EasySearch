package com.search.adapter.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Data;

import java.util.List;

@Data
public class DocsResponceVo<T extends CommunityBaseVo> {
    List<T> records;
    Integer pageSize;
    Integer page;
    String keyword;
    long total;
    public DocsResponceVo(List<T> records, Integer pageSize, Integer page, String keyword) {
        this.records = records;
        this.pageSize = pageSize;
        this.page = page;
        this.keyword = keyword;
    }
}
