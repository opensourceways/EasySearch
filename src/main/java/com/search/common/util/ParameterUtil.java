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
package com.search.common.util;


import java.util.Locale;

public final class ParameterUtil {

    // Private constructor to prevent instantiation of the utility class
    private ParameterUtil() {
        // private constructor to hide the implicit public one
        throw new AssertionError("ParameterUtil class cannot be instantiated.");
    }

    /**
     * checck param.
     *
     * @param lang 语言.
     * @return String.
     */
    public static String vaildLang(String lang) {
        lang = lang == null ? "zh" : lang;
        if (!lang.equalsIgnoreCase("zh") && !lang.equalsIgnoreCase("en")) {
            throw new IllegalArgumentException("Invalid lang parameter");
        }
        return lang.toLowerCase(Locale.ROOT);
    }

    /**
     * checck param.
     *
     * @param community 社区.
     * @return String.
     */
    public static String vaildCommunity(String community) {
        if (!"mindspore".equalsIgnoreCase(community) && !"opengauss".equalsIgnoreCase(community)
                && !"openlookeng".equalsIgnoreCase(community) && !"xihe".equalsIgnoreCase(community)) {
            throw new IllegalArgumentException("Invalid community parameter");
        }
        return community.toLowerCase(Locale.ROOT);
    }

    /**
     * checck param.
     *
     * @param ecosystemType ecosystemType.
     * @return String.
     */
    public static String vaildEcosystemType(String ecosystemType) {
        ecosystemType = ecosystemType == null ? "Library" : ecosystemType;
        if (!"Library".equalsIgnoreCase(ecosystemType) && !"Tutorial".equalsIgnoreCase(ecosystemType) && !"Model".equalsIgnoreCase(ecosystemType)) {
            throw new IllegalArgumentException("Invalid ecosystemType parameter");
        }
        return ecosystemType;
    }

    /**
     * checck param.
     *
     * @param page page.
     * @return String.
     */
    public static String vaildPage(String page) {
        page = page == null ? "1" : page;
        if (Integer.parseInt(page) > 100 || Integer.parseInt(page) < 1) {
            throw new IllegalArgumentException("Invalid page parameter");
        }
        return page;
    }

    /**
     * checck param.
     *
     * @param pageSize page.
     * @return String.
     */
    public static String vaildPageSize(String pageSize) {
        pageSize = pageSize == null ? "10" : pageSize;
        if (Integer.parseInt(pageSize) > 100 || Integer.parseInt(pageSize) < 1) {
            throw new IllegalArgumentException("Invalid vaildPageSize parameter");
        }
        return pageSize;
    }

}
