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
package com.search.common.thread;


import com.search.common.util.ObjectMapperUtil;

import java.util.Optional;

/**
 * 当前线程请求头信息
 */
public final class ThreadLocalCache {
    /**
     * 私有化构造方法.
     */
    private ThreadLocalCache() {

    }

    /**
     * ThreadLocal.
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * ThreadLocal set.
     */
    public static void put(String str) {
        THREAD_LOCAL.set(str);
    }

    /**
     * ThreadLocal get.
     */
    public static String get() {
        return THREAD_LOCAL.get();
    }

    /**
     * ThreadLocal remove.
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * getHeaderInfo.
     */
    public static HeaderInfo getHeaderInfo() {
        return Optional.ofNullable(THREAD_LOCAL.get()).map(json -> ObjectMapperUtil.toObject(HeaderInfo.class, json)).orElse(
                new HeaderInfo());
    }

    /**
     * getDataSource.
     */
    public static String getDataSource() {
        return getHeaderInfo().getDataSource();
    }

    /**
     * put Source on ThreadLocalCache .
     */
    public static void putSource(String source) {
        HeaderInfo headerInfo = getHeaderInfo();
        headerInfo.setDataSource(source);
        put(ObjectMapperUtil.writeValueAsString(headerInfo));
    }

}
