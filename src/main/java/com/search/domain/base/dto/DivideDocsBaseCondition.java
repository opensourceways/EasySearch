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
package com.search.domain.base.dto;

import com.search.adapter.condition.DevideCondition;
import lombok.Data;

@Data
public class DivideDocsBaseCondition {
    /**
     * 输入框关键字.
     */
    private String keyword;
    /**
     * 版本.
     */
    private String version;
    /**
     * 数据索引.
     */
    protected String index;
    /**
     * 开始页码.
     */
    protected Integer pageFrom;
    /**
     * 第几页.
     */
    protected Integer page;
    /**
     * 页数.
     */
    protected Integer pageSize;
    /**
     * 类型.
     */
    protected String type;

    /**
     * 有参构造，初始化condition
     *
     * @param index     数据索引 .
     * @param type      类型.
     * @param condition 前台请求封装条件
     * @return DivideDocsBaseCondition.
     */
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
