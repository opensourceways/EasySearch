package com.search.domain.openmind.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class DocsOpenmindCondition extends SearchDocsBaseCondition {
    private List<OpenmindFilter> filter;

    public DocsOpenmindCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setOpenmindFilter(condition);
    }


    public void setOpenmindFilter(DocsCondition condition) {
        ArrayList<OpenmindFilter> openmindFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter()))
            condition.getLimit().stream().forEach(a -> {
                OpenmindFilter filter = new OpenmindFilter();
                filter.setDocsType(a.getDocsType());
                openmindFilters.add(filter);
            });
        this.filter = openmindFilters;
    }


    @Data
    private class OpenmindFilter {
        String docsType;
    }
}
