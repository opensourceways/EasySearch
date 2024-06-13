package com.search.docsearch.controller.community;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "community.controller.enabled", havingValue = "true")
public class MyErrorController implements ErrorController {

    private final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public String errorHtml(HttpServletRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        return "{\"code\":" + code + ",\"msg\": \"error\"}";
    }
}