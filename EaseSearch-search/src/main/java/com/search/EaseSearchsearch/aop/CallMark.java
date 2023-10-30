package com.search.EaseSearchsearch.aop;

import lombok.Data;

import java.time.Instant;

@Data
public class CallMark {
    private Instant lastCallTime;
    private int callCount;

}
