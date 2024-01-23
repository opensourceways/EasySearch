package com.search.docsearch.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;


import java.io.IOException;

/**
 *
 */
@Slf4j
public class TraceIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        MDC.put("traceId", String.valueOf(System.currentTimeMillis()));
        log.debug("*********************************TraceIdFilter被使用**************************");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

}
