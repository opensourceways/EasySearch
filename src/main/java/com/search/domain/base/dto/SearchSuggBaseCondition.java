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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSuggBaseCondition {
    /**
     * 输入框关键字.
     */
    protected String keyword;

    /**
     * 建议返回的更正词的最大数量.
     */
    protected Integer size;
    /**
     * 数据索引.
     */
    protected String index;

    /**
     * termSuggestion 字段.
     */
    protected String fieldname;
    /**
     * 建议文本术语必须具有的最小长度才能进行更正。默认值为 4.
     */
    protected Integer minWordLength;
    /**
     * 必须满足搜索词的前端的多少个字符.
     */
    protected Integer prefixLength;
    /**
     * 分析器.
     */
    protected String analyzer;

}
