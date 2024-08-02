package com.search.docsearch.config;


import com.search.docsearch.entity.software.SoftwareSysResult;
import com.search.docsearch.entity.vo.SysResult;
import com.search.docsearch.except.ControllerException;
import com.search.docsearch.except.ServiceException;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.except.TrustManagerException;
import com.search.docsearch.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
@Configuration
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ControllerException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void controllerException(HttpServletRequest request, HttpServletResponse response, ControllerException e) {
        log.error("Controller异常:", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void serviceException(HttpServletRequest request, HttpServletResponse response, ServiceException e) {
        log.error("Service异常:{}", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }

    @ExceptionHandler(value = TrustManagerException.class)
    @ResponseStatus(HttpStatus.OK)
    public void trustManagerException(HttpServletRequest request, HttpServletResponse response, TrustManagerException e) {
        log.error("TrustManager异常:{}", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }

    @ExceptionHandler(value = ServiceImplException.class)
    @ResponseStatus(HttpStatus.OK)
    public void serviceImplException(HttpServletRequest request, HttpServletResponse response, ServiceImplException e) {
        log.error("ServiceImpl常:{}", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void methodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        log.error("参数校验异常:", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        HashSet<String> errorFieldSet = new HashSet<>();
        bindingResult.getFieldErrors().stream().forEach(fieldError -> errorFieldSet.add(fieldError.getField()));
        errorFieldSet.stream().forEach(field -> sb.append(field).append("格式异常;"));
        if (request.getRequestURI().contains("/software")) {
            responseJson(request, response, SoftwareSysResult.fail(sb.toString(), null));
        } else {
            responseJson(request, response, SysResult.fail(sb.toString(), null));
        }
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void illegalArgumentException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException e) {
        log.error("参数异常:{}", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }


    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void sqlException(HttpServletRequest request, HttpServletResponse response, SQLException e) {
        log.error("sql异常:{}", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("异常:", e.getMessage());
        responseJson(request, response, SysResult.fail("查询失败", null));
    }

    /**
     * 响应json格式字符串
     *
     * @param request   {HttpServletRequest}
     * @param response  {HttpServletResponse}
     * @param sysResult {SysResult}
     */
    private void responseJson(HttpServletRequest request, HttpServletResponse response, Object sysResult) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(JacksonUtils.writeValueAsString(sysResult));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
