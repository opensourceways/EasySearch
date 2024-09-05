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

package com.search.common.aop.log;

import com.search.common.util.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ManagementLogAOP {

    /**
     * Autowired HttpServletRequest for handling HTTP request information.
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * Autowired HttpServletResponse for handling HTTP response information.
     */
    @Autowired
    private HttpServletResponse response;

    /**
     * Defines the pointcut for methods in specific packages.
     */
    @Pointcut("execution(* com.search.controller.*.*(..)) "
            + "|| execution(* com.search.common.exception.GlobalExceptionHandler.*(..))")
    public void pointcut() {
    }

    /**
     * Advice method called after a method in the specified packages successfully returns.
     *
     * @param joinPoint    The JoinPoint representing the intercepted method.
     * @param returnObject The object returned by the intercepted method.
     */
    @AfterReturning(pointcut = "pointcut()", returning = "returnObject")
    public void afterReturning(final JoinPoint joinPoint, final Object returnObject) {
        LogUtil.managementOperate(joinPoint, request, response, returnObject);
    }

}

