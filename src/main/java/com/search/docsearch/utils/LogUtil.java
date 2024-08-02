package com.search.docsearch.utils;

import com.alibaba.fastjson2.JSON;
import com.search.docsearch.aop.LogAction;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import jakarta.servlet.http.HttpServletRequest;
public class LogUtil {

    public static final String TRACE_ID = "TRACE_ID";

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void returnOperate(JoinPoint joinPoint, int status, HttpServletRequest request, long startTime) {
        returnLog log = new returnLog();
        log.setTraceId(MDC.get(TRACE_ID));

        long endTime = System.currentTimeMillis();
        log.setTimeConsumed((endTime - startTime) + "ms");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date(startTime);
        String str = sdf.format(date);
        log.setStartTime(str);
        log.setUserId(System.getenv("NO_ID_USER"));

        log.setAccessIp(ClientIPUtil.getClientIpAddress(request));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAction action = method.getAnnotation(LogAction.class);
        if (null != action) {
            log.setType(action.type());
            log.setOperationResource(action.OperationResource());
        }

        log.setFunc(String.format(Locale.ROOT, "%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));

        log.setRequestUrl(request.getRequestURI());

        log.setMethod(request.getMethod());

        log.setStatus(status);

        log.setArgs(joinPoint.getArgs());

        if (status != 200) {
            log.setMessage("ERROR");
        } else {
            log.setMessage("SUCCESS");
        }
        logger.info("operationLog:{}");
    }
    /**
     * format logging parameter.
     *
     * @param input The input pramater
     * @return formatedOutput The safe output logging parmeter
     */

    public static String formatCodeString(String input) {
        if (input == null) {
            return input;
        }

        String formatedOutput = input.replace("\r", "\\r").replace("\n", "\\n").replace("\u0008", "\\u0008")
                .replace("\u000B", "\\u000B")
                .replace("\u000C", "\\u000C")
                .replace("\u007F", "\\u007F")
                .replace("\u0009", "\\u0009");

        return formatedOutput;
    }

    @Data
    public static class returnLog {

        private String traceId;

        private String startTime;

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

        private Object[] args;

        private String timeConsumed;

    }

}
