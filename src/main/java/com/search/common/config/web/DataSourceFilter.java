package com.search.common.config.web;


import com.search.adapter.enums.SearchSourceEnum;
import com.search.common.constant.SourceConstant;
import com.search.common.exception.ParamErrorException;
import com.search.common.thread.ThreadLocalCache;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Objects;


@Slf4j
public class DataSourceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String source = request.getHeader(SourceConstant.REQUETS_HEADER_SOURCE);
        SearchSourceEnum requestTypeByDatasource = SearchSourceEnum.getRequestTypeByDatasource(source);
        if (Objects.isNull(requestTypeByDatasource)) {
            throw new ParamErrorException("the value of datasource name: openEuler, openGauss, openMind, mindSpore,softcenter");
        }
        ThreadLocalCache.putSource(source);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
