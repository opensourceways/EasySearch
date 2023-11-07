package com.search.docsearch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "search-interceptor")
public class SearchInterceptor implements HandlerInterceptor {

    private static List<String> allowedPath;

    public void setAllowedPath(List<String> allowedPath) {
        SearchInterceptor.allowedPath = allowedPath;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();

        if (allowedPath.contains(url)) {
            return true;
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return false;
    }
}
