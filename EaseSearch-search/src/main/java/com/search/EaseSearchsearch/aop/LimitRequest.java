package com.search.EaseSearchsearch.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequest {
    int callTime() default 1;
    int callCount() default 20;
}
