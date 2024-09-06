package com.search.adapter.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Data;

import java.util.List;

@Data
public class SortResponceVo<T extends CommunityBaseVo> {
    List<T> records;
    Integer pageSize;
    Integer page;
    Long count;

    public SortResponceVo(List<T> records, Integer pageSize, Integer page, Long count) {
        this.records = records;
        this.pageSize = pageSize;
        this.page = page;
        this.count = count;
    }
}
