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
import org.hibernate.validator.constraints.Range;


@Getter
@Setter
public class SortCondition {


    /**
     * condition field of category.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String category;

    /**
     * condition field of tags.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String tags;


    /**
     * condition field of archives.
     */
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REGEXP, message = SearchConstant.VALID_MESSAGE)
    private String archives;

    /**
     * type.
     */
    @Size(max = 15)
    private String type;


    /**
     * 语言,用于选择数据源.
     */
    @NotBlank(message = SearchConstant.LANG_NULL_MESSAGE)
    @Pattern(regexp = SearchConstant.LANG_REGEXP)
    private String lang;
    /**
     * Page number within the range of PackageConstant.MIN_PAGE_NUM to PackageConstant.
     * MAX_PAGE_NUM, default value is 1.
     */
    @Range(min = SearchConstant.MIN_PAGE_NUM,
            max = SearchConstant.MAX_PAGE_NUM,
            message = SearchConstant.PAGE_RANGE_MESSAGE)
    private int page = 1;
    /**
     * Page size within the range of PackageConstant.MIN_PAGE_SIZE to PackageConstant.
     * MAX_PAGE_SIZE, default value is 10.
     */
    @Range(min = SearchConstant.MIN_PAGE_SIZE,
            max = SearchConstant.MAX_PAGE_SIZE,
            message = SearchConstant.PAGESIZE_RANGE_MESSAGE)
    private int pageSize = 10;


    /**
     * Page size within the range of PackageConstant.MIN_PAGE_SIZE to PackageConstant.
     * MAX_PAGE_SIZE, default value is 10.
     */
    @NotBlank(message = "keyword can not be null")
    @Pattern(regexp = SearchConstant.SEARCH_KEYWORD_REGEXP,
            message = SearchConstant.SEARCH_KEYWORD_MESSAGE)
    @Size(max = 100)
    private String keyword;

    /**
     * Page size within the range of PackageConstant.MIN_PAGE_SIZE to PackageConstant.
     * MAX_VERSION_SIZE, default value is 10.
     */
    @Pattern(regexp = SearchConstant.VERSION_REGEXP, message = "Version format is invalid.")
    @Size(max = 50)
    private String version;
}
