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

public final class SourceConstant {

    // Private constructor to prevent instantiation of the SourceConstant class
    private SourceConstant() {
        // private constructor to hide the implicit public one
        throw new AssertionError("SourceConstant class cannot be instantiated.");
    }

    /**
     * Constant of REQUETS_HEADER_SOURCE.
     */
    public static final String REQUETS_HEADER_SOURCE = "source";
    /**
     * Constant of SOURCE_OPENEULER.
     */
    public static final String SOURCE_OPENEULER = "openeuler";
    /**
     * Constant of SOURCE_OPENGAUSS.
     */
    public static final String SOURCE_OPENGAUSS = "opengauss";
    /**
     * Constant of SOURCE_OPENMIND.
     */
    public static final String SOURCE_OPENMIND = "openmind";
    /**
     * Constant of SOURCE_MINDSPORE.
     */
    public static final String SOURCE_MINDSPORE = "mindspore";
    /**
     * Constant of SOURCE_SOFTCENTER.
     */
    public static final String SOURCE_SOFTCENTER = "softcenter";
}
