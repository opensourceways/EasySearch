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
package com.search.common.constant;

public final class SearchConstant {

    // Private constructor to prevent instantiation of the SearchConstant class
    private SearchConstant() {
        // private constructor to hide the implicit public one
        throw new AssertionError("SearchConstant class cannot be instantiated.");
    }

    /**
     * language allowed.
     */
    public static final String LANG_REGEXP = "((^zh$|^en$|^ZH$|^EN$))";

    /**
     * version  regexp  allowed.
     */
    public static final String VERSION_REGEXP = "^[\\x20a-zA-Z0-9.\\-_ ]*$";


    /**
     * keyword regexp allowed.
     */
    public static final String SEARCH_KEYWORD_REGEXP = "^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_ ]+$";


    /**
     * common string regexp used to match input string.
     */
    public static final String VALID_STR_REGEXP = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$";

    /**
     * used to match input string.
     */
    public static final String DATATYPE_REGEXP = "((^all$|^rpmpkg$|^apppkg$|^epkgpkg$|^appversion$|^$))";

    /**
     * used to match input string.
     */
    public static final String NAMEORDER_REGEXP = "((^desc$|^asc$|^$))";


    /**
     * language null message.
     */
    public static final String LANG_NULL_MESSAGE = "lang can not be null";

    /**
     * page 超出范围错误信息.
     */
    public static final String PAGE_RANGE_MESSAGE = "page must be greater than 0 and less than 1000 ";

    /**
     * pagesize 超出范围错误信息.
     */
    public static final String PAGESIZE_RANGE_MESSAGE = "pageSize must be greater than 5 and less than 50  ";

    /**
     * keyword regexp allowed.
     */
    public static final String SEARCH_KEYWORD_MESSAGE =
            "Include only letters, digits, and special characters(_-()$.), Contain 1 to 100 characters.";

    /**
     * VALID_MESSAGE, error message.
     */
    public static final String VALID_MESSAGE = "Null or string. String includes only letters, digits, and special "
            + "characters(_-+()$.,)";
    /**
     * VALID_MESSAGE, error message.
     */
    public static final String WANT_NULL_MESSAGE = "want can not be null";


    /**
     * Maximum page number allowed.
     */
    public static final int MAX_PAGE_NUM = 1000;

    /**
     * Minimum page number allowed.
     */
    public static final int MIN_PAGE_NUM = 1;


    /**
     * Maximum page size allowed.
     */
    public static final int MAX_PAGE_SIZE = 50;


    /**
     * Minimum page size allowed.
     */
    public static final int MIN_PAGE_SIZE = 5;

    /**
     * Maximum field length allowed.
     */
    public static final int MAX_FIELD_LENGTH = 1000;


    /**
     * INDEX_CONNECT, ES index  connect.
     */
    public static final String INDEX_CONNECT = "_articles_";


    /**
     * HTTPS_PREFIX, Referer pass prefix.
     */
    public static final String HTTPS_PREFIX = "https://";
}



