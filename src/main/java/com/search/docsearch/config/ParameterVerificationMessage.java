package com.search.docsearch.config;

import com.search.docsearch.entity.vo.SysResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ParameterVerificationMessage
 */
@RestControllerAdvice
public class ParameterVerificationMessage {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    public SysResult MethodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder sb = new StringBuilder("Parameter verification failed:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {

            sb.append("[").append(fieldError.getField()).append("->").append(fieldError.getDefaultMessage()).append("]");
        }
        String msg = sb.toString();

        return SysResult.parameterVerificationFailed(msg);
    }

}