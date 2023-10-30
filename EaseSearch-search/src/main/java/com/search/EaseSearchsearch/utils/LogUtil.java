package com.search.EaseSearchsearch.utils;

import com.alibaba.fastjson2.JSON;
import com.search.EaseSearchsearch.aop.LogAction;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    public static final String TRACE_ID = "TRACE_ID";

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void returnOperate(JoinPoint joinPoint, int status, String message, HttpServletRequest request) {
        returnLog log = new returnLog();
        log.setTraceId(MDC.get(TRACE_ID));

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setTime(dateTime.format(formatter));

        log.setUserId(System.getenv("NO_ID_USER"));

        log.setAccessIp(ClientIPUtil.getClientIpAddress(request));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAction action = method.getAnnotation(LogAction.class);
        if (null != action) {
            log.setType(action.type());
            log.setOperationResource(action.OperationResource());
        }

        log.setFunc(String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));

        log.setRequestUrl(request.getRequestURI());

        log.setMethod(request.getMethod());

        log.setStatus(status);

        if (status != 200) {
            log.setMessage("ERROR");
        } else {
            log.setMessage("SUCCESS");
        }

        String jsonLog = JSON.toJSONString(log);
        logger.info("operationLog:{}", jsonLog);
    }

    @Data
    public static class returnLog implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String traceId;

        private String time;

        private String userId;

        private String accessIp;

        private String type;

        private String OperationResource;

        private String func;

        private String requestUrl;

        private String method;

        private int status;

        private String message;

        private String ErrorLog;

    }

}
