package com.search.domain.mindspore.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class DocsMindsporeCondition extends SearchDocsBaseCondition {
    private List<MindsporeLimit> limit;
    private List<MindsporeFilter> filter;

    public DocsMindsporeCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setMindsporeLimit(condition);
        setMindsporeFilter(condition);
    }


    public void setMindsporeLimit(DocsCondition condition) {
        ArrayList<MindsporeLimit> mindsporeLimits = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter()))
            condition.getLimit().stream().forEach(a -> {
                MindsporeLimit mindsporeLimit = new MindsporeLimit();
                mindsporeLimit.setComponents(a.getComponents());
                mindsporeLimit.setVersion(a.getVersion());
                mindsporeLimit.setName(a.getName());
                mindsporeLimits.add(mindsporeLimit);
            });
        this.limit = mindsporeLimits;
    }


    public void setMindsporeFilter(DocsCondition condition) {
        ArrayList<MindsporeFilter> mindsporeFilters = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter()))
            condition.getLimit().stream().forEach(a -> {
                MindsporeFilter mindsporeFilter = new MindsporeFilter();
                mindsporeFilter.setComponents(a.getComponents());
                mindsporeFilter.setVersion(a.getVersion());
                mindsporeFilter.setName(a.getName());
                mindsporeFilters.add(mindsporeFilter);
            });
        this.filter = mindsporeFilters;
    }

    @Data
    public class MindsporeLimit {
        String components;
        String version;
        String name;
    }


    @Data
    public class MindsporeFilter {
        String components;
        String version;
        String name;
    }
}
