package com.search.docsearch.aop;

import com.search.docsearch.utils.LogUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Component
public class ManagementOperationLogAOP {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Pointcut("execution(* com.search.docsearch.controller..*.*(..))")
    public void pointCut() {
    }


    @Around(value = "pointCut()")
    public Object afterReturning(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        LogUtil.returnOperate(joinPoint, response.getStatus(), request, startTime);
        return result;
    }

}
