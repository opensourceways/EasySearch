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
public class DivideSortBaseCondition {
    /**
     * 输入框关键字.
     */
    protected String keyword;
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
}
