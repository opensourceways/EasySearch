

package com.search.docsearch.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimitRedis {
    /**
     * Specifies the time limit in seconds (default value: 30s).
     *
     * @return The time limit in seconds.
     */
    long period() default 30;

    /**
     * Specifies the number of allowed requests (default value: 5).
     *
     * @return The number of allowed requests.
     */
    long count() default 5;

}
