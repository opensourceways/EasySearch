/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.domain.opengauss.dto;

import com.search.adapter.condition.DocsCondition;
import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Data
public class DocsOpengaussCondition extends SearchDocsBaseCondition {
    /**
     * Openeuler Limit list.
     */
    private List<OpengaussLimit> limit;

    /**
     * 有参构造，初始化DocsOpengaussCondition
     *
     * @param index     数据索引 .
     * @param condition 前台请求封装条件
     * @return DocsOpengaussCondition.
     */
    public DocsOpengaussCondition(String index, DocsCondition condition) {
        super.index = index;
        super.pageFrom = (condition.getPage() - 1) * condition.getPageSize();
        super.page = condition.getPage();
        super.pageSize = condition.getPageSize();
        super.keyword = condition.getKeyword();
        super.type = condition.getType();
        setOpengaussLimit(condition);
    }

    /**
     * 根据DocsCondition封装社区limit obj
     *
     * @param condition 前台请求封装条件.
     */
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

    /**
     * OpengaussLimit obj.
     */
    @Getter
    @Setter
    private class OpengaussLimit {
        String type;
        String version;
    }


}
