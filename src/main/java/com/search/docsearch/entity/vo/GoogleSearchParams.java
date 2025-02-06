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
package com.search.docsearch.entity.vo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GoogleSearchParams {

    /**
     * The keyword to search for.
     */
    @NotBlank(message = "keyword can not be null")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_ ]+$", message = "Include only letters, digits, and special characters(_-()$.), Contain 1 to 100 characters.")
    @Size(max = 100)
    private String keyWord;
    /**
     * The starting index for the search results to return.
     */
    @NotBlank(message = "start can not be null")
    @Pattern(regexp = "\\d+", message = "start Must be numeric")
    @Size(max = 100)
    private String start;
    /**
     * The number of search results to return per page.
     */
    @Pattern(regexp = "\\d+", message = "num Must be numeric")
    @Size(max = 10)
    private String num;
    /**
     * The language restriction for the search.
     */
    private String lr;
    public String buildUrl(String url, String api, String cx) {
        String urlString = url + "?key=" + api + "&q=" + keyWord + "&cx=" + cx
                + "&start=" + start + "&num=" + num + "&lr=" + lr;
        return urlString;
    }
}