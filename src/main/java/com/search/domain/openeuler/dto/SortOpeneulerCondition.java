package com.search.domain.openeuler.dto;

import com.search.adapter.condition.SortCondition;
import com.search.domain.base.dto.SearchSortBaseCondition;
import lombok.Data;

@Data
public class SortOpeneulerCondition extends SearchSortBaseCondition {
    String category;
    String tags;
    String archives;

    public SortOpeneulerCondition(String index, SortCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        this.category = condition.getCategory();
        this.tags = condition.getTags();
        this.archives = condition.getArchives();
    }
}
