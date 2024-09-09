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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FieldCondition {
    /**
     * condition field of type.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String type;
    /**
     * condition field of components.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String components;

    /**
     * condition field of name.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String name;

    /**
     * condition field of docsType.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String docsType;

    /**
     * condition field of dataType.
     */
    @Size(max = 10)
    @Pattern(regexp = SearchConstant.DATATYPE_REGEXP)
    private String dataType;

    /**
     * condition field of version.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String version;


    /**
     * condition field of os.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String os;

    /**
     * condition field of arch.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String arch;

    /**
     * condition field of category.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String category;

    /**
     * condition field of keywordType.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String keywordType;

    /**
     * condition field of nameOrder.
     */
    @Size(max = 5)
    @Pattern(regexp = SearchConstant.NAMEORDER_REGEXP)
    private String nameOrder;

    /**
     * condition field of eulerOsVersion.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String eulerOsVersion;

    /**
     * condition field of tags.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String tags;
}
