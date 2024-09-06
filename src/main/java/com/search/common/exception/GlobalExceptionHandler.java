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

package com.search.common.exception;

import com.search.common.entity.ResponceResult;
import com.search.common.util.ObjectMapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Logger instance for logging exceptions.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles exceptions of type TrustManagerException.
     *
     * @param e The TrustManagerException to handle
     * @return ResponceResult containing details about the exception
     */
    @ExceptionHandler(value = TrustManagerException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponceResult trustManagerException(TrustManagerException e) {
        LOGGER.error("TrustManager异常:{}", e.getMessage());
        return ResponceResult.fail("查询失败", null);
    }

    /**
     * Handles exceptions of type MethodArgumentNotValidException.
     *
     * @param e The MethodArgumentNotValidException to handle
     * @return ResponceResult containing details about the exception
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponceResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error("参数校验异常:", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append("[").append(fieldError.getField()).append("->").append(fieldError.getDefaultMessage()).append("]");
        }
        return ResponceResult.fail("查询失败", null);
    }

    /**
     * Handles exceptions of type IllegalArgumentException.
     *
     * @param e The IllegalArgumentException to handle
     * @return ResponceResult containing details about the exception
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponceResult illegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("参数异常:{}", e.getMessage());
        return ResponceResult.fail(e.getMessage(), null);
    }

    /**
     * Handles exceptions of type Exception.
     *
     * @param e The Exception to handle
     * @return ResponceResult containing details about the exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponceResult exception(Exception e) {
        LOGGER.error("异常:", e.getMessage());
        return ResponceResult.fail("查询失败", null);
    }

    /**
     * 响应json格式字符串
     *
     * @param request   {HttpServletRequest}
     * @param response  {HttpServletResponse}
     * @param sysResult {SysResult}
     */
    private void responseJson(HttpServletRequest request, HttpServletResponse response, ResponceResult sysResult) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(ObjectMapperUtil.writeValueAsString(sysResult));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
