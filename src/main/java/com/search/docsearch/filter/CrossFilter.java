package com.search.docsearch.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

/**
 * 跨域过滤器
 *
 */
@Slf4j
public class CrossFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.getSession(false);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST,PATCH,GET,OPTIONS,DELETE,PUT");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept,Authorization,token");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("X-Frame-Options","SAMEORIGIN");
        String sessionId = request.getSession().getId();
        response.setHeader("SET-COOKIE","SESSIONID="+sessionId+";HttpOnly");
        log.debug("*********************************跨域过滤器被使用**************************");
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
