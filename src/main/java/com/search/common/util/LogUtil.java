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

package com.search.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.common.aop.log.ManagementLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class LogUtil {

    // Private constructor to prevent instantiation of the utility class
    private LogUtil() {
        // private constructor to hide the implicit public one
        throw new AssertionError("LogUtil class cannot be instantiated.");
    }

    /**
     * Logger instance for LogUtil.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
    /**
     * ObjectMapper instance for JSON serialization and deserialization.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Perform a management operation.
     *
     * @param joinPoint    The JoinPoint object
     * @param request      The HttpServletRequest object
     * @param response     The HttpServletResponse object
     * @param returnObject The object returned from the operation
     */
    @SneakyThrows
    public static void managementOperate(final JoinPoint joinPoint,
                                         final HttpServletRequest request,
                                         final HttpServletResponse response,
                                         final Object returnObject) {
        ManagementLog log = new ManagementLog();
        log.setType("OmOperate");

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setTime(dateTime.format(formatter));

        log.setFunc(String.format(Locale.ROOT, "%s.%s", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()));

        log.setRequestUrl(request.getRequestURI());
        log.setMethod(request.getMethod());

        log.setAppIP(ClientUtil.getClientIpAddress(request));

        if (returnObject instanceof ResponseEntity) {
            ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) returnObject;
            log.setStatus(responseEntity.getStatusCode().value());
            /*if (responseEntity.getBody() instanceof ResultVo) {
                ResultVo body = (ResultVo) responseEntity.getBody();
                Object msg = body.getMsg();
                log.setMessage((msg == null) ? "" : msg.toString());
            }*/
        }
        log.setOperator("");

        String jsonLog = OBJECT_MAPPER.writeValueAsString(log);
        LOGGER.info("operationLog:{}", jsonLog);
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
}
