package com.search.docsearch.filter;

import com.search.docsearch.thread.ThreadLocalCache;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ContentTypeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String userToken = request.getHeader("Token");
        if(Objects.nonNull(userToken)){
            ThreadLocalCache.put(userToken);
        }
       /* if (request.getMethod().equalsIgnoreCase("POST")) {
            String contentType = request.getContentType();
            if (!contentType.startsWith("application/json")) {
                log.info("request reject,post request Content-Type error：" + contentType);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }*/
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
