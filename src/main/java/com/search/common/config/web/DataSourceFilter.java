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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
public class DataSourceFilter implements Filter {
    /**
     * Referer pass allowSources.
     */
    private String allowSources;

    /**
     * check source.
     *
     * @param servletRequest  The request.
     * @param servletResponse The response.
     * @param filterChain     The filterChain.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String source = request.getHeader(SourceConstant.REQUETS_HEADER_SOURCE);
        if (this.allowSources != null) {
            List<String> allowSourcesList = Arrays.asList(allowSources.split(","));
            if (!allowSourcesList.contains(source)) {
                throw new ParamErrorException("current support dateSource:" + allowSources);
            }
        }
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

    /**
     * constructor.
     *
     * @param allowSources allowSources.
     */
    public DataSourceFilter(String allowSources) {
        this.allowSources = allowSources;
    }
}
