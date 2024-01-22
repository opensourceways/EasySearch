package com.search.docsearch.aop;

import com.search.docsearch.utils.LogUtil;
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
public class ManagementOperationLogAOP {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Pointcut("execution(* com.search.docsearch.controller..*.*(..))")
    public void pointCut() {
    }


    @AfterReturning(value = "pointCut()", returning = "returnObject")
    public void afterReturning(JoinPoint joinPoint, Object returnObject) {
        LogUtil.returnOperate(joinPoint, response.getStatus(), "", request);
    }

}
