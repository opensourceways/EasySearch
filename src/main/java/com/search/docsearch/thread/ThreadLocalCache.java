package com.search.docsearch.thread;


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


}
