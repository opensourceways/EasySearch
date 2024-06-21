package com.search.docsearch.common.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequest {
    /**
     * 默认调用时间.
     *
     * @return 默认调用时间
     */
    int callTime() default 1;

    /**
     * 默认调用次数限制.
     *
     * @return 默认调用次数限制
     */
    int callCount() default 20;
}
