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
package com.search.adapter.condition;

import com.search.common.constant.SearchConstant;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WordConditon {
    /**
     * 输入语言.
     */
    private String query;
    /**
     * 语言,用于选择数据源.
     */
    @Pattern(regexp = SearchConstant.LANG_REGEXP)
    private String lang;

}
