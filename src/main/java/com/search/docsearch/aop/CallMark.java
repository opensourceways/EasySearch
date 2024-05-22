package com.search.docsearch.aop;

import java.time.Instant;

import lombok.Data;

@Data
public class CallMark {
    /**
     * 上次调用的时间戳.
     */
    private Instant lastCallTime;

    /**
     * 调用次数计数.
     */
    private int callCount;

}
