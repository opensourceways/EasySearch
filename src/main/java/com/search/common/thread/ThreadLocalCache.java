package com.search.common.thread;


import com.search.common.util.ObjectMapperUtil;

import java.util.Optional;

/**
 * 当前线程请求头信息
 */
public final class ThreadLocalCache {

    private ThreadLocalCache() {

    }

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void put(String str) {
        THREAD_LOCAL.set(str);
    }

    public static String get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static HeaderInfo getHeaderInfo() {
        return Optional.ofNullable(THREAD_LOCAL.get()).map(json -> ObjectMapperUtil.toObject(HeaderInfo.class, json)).orElse(
                new HeaderInfo());
    }

    public static String getDataSource() {
        return getHeaderInfo().getDataSource();
    }


    public static void putSource(String source) {
        HeaderInfo headerInfo = getHeaderInfo();
        headerInfo.setDataSource(source);
        put(ObjectMapperUtil.writeValueAsString(headerInfo));
    }

}
