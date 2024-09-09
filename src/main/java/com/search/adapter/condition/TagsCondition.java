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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TagsCondition {

    /**
     * 语言,用于选择数据源.
     */
    @NotBlank(message = SearchConstant.LANG_NULL_MESSAGE)
    @Pattern(regexp = SearchConstant.LANG_REGEXP)
    private String lang;

    /**
     * condition field of category.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String category;
    /**
     * condition field of want.
     */
    @NotBlank(message = SearchConstant.WANT_NULL_MESSAGE)
    @Size(max = 20)
    private String want;
    /**
     * condition object of condition.
     */
    private FieldCondition condition;
}
