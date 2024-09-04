package com.search.domain.opengauss.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class DocsOpengaussCondition extends SearchDocsBaseCondition {
    private List<OpengaussLimit> limit;


    public DocsOpengaussCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setOpengaussLimit(condition);
    }


    public void setOpengaussLimit(DocsCondition condition) {
        ArrayList<OpengaussLimit> opengaussLimits = new ArrayList<>();
        if (Objects.nonNull(condition.getFilter()))
            condition.getLimit().stream().forEach(a -> {
                OpengaussLimit opengaussLimit = new OpengaussLimit();
                opengaussLimit.setType(a.getType());
                opengaussLimit.setVersion(a.getVersion());
                opengaussLimits.add(opengaussLimit);
            });
        this.limit = opengaussLimits;
    }

    @Data
    private class OpengaussLimit {
        String type;
        String version;
    }


}
