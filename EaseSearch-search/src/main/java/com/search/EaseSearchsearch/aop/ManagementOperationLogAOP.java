package com.search.EaseSearchsearch.aop;

import com.search.EaseSearchsearch.utils.LogUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class ManagementOperationLogAOP {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Pointcut("execution(* com.search.EaseSearchsearch.controller..*.*(..))")
    public void pointCut() {
    }


    @AfterReturning(value = "pointCut()", returning = "returnObject")
    public void afterReturning(JoinPoint joinPoint, Object returnObject) {
        LogUtil.returnOperate(joinPoint, response.getStatus(), "", request);
    }

}
