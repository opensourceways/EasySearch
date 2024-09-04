package com.search.domain.openeuler.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class DocsOpeneulerCondition extends SearchDocsBaseCondition {
    private List<OpeneulerLimit> limit;
    private List<OpeneulerFilter> filter;

    public DocsOpeneulerCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setOpeneulerLimit(condition);
        setOpeneulerFilter(condition);
    }


    public void setOpeneulerLimit(DocsCondition condition) {
        ArrayList<OpeneulerLimit> openeulerLimits = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter()))
            condition.getLimit().stream().forEach(a -> {
                OpeneulerLimit openeulerLimit = new OpeneulerLimit();
                openeulerLimit.setType(a.getType());
                openeulerLimit.setVersion(a.getVersion());
                openeulerLimits.add(openeulerLimit);
            });
        this.limit = openeulerLimits;
    }


    public void setOpeneulerFilter(DocsCondition condition) {
        ArrayList<OpeneulerFilter> openeulerFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter()))
            condition.getLimit().stream().forEach(a -> {
                OpeneulerFilter openeulerFilter = new OpeneulerFilter();
                openeulerFilter.setType(a.getType());
                openeulerFilter.setVersion(a.getVersion());
                openeulerFilters.add(openeulerFilter);
            });
        this.filter = openeulerFilters;
    }

    @Data
    private class OpeneulerLimit {
        String type;
        String version;
    }


    @Data
    private class OpeneulerFilter {
        String type;
        String version;
    }
}
