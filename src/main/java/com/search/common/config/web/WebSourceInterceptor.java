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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Slf4j
public class WebSourceInterceptor implements HandlerInterceptor {
    /**
     * constructor.
     *
     * @param allowSources allow Sources.
     */
    public WebSourceInterceptor(String allowSources) {
        this.allowSources = allowSources;
    }

    /**
     * Referer pass allowSources.
     */
    private String allowSources;

    /**
     * @param servletRequest  – current HTTP request
     * @param servletResponse – current HTTP response
     * @param handler         – chosen handler to execute, for type and/or instance evaluation
     * @return : true if the execution chain should proceed with the next interceptor or the handler itself. Else,
     * DispatcherServlet assumes that this interceptor has already dealt with the response itself.
     */
    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String source = request.getHeader(SourceConstant.REQUETS_HEADER_SOURCE);
        if (StringUtils.hasText(this.allowSources)) {
            List<String> allowSourcesList = Arrays.asList(allowSources.split(","));
            //没传source会选择默认配置,用于更新的时候平替
            if (StringUtils.isEmpty(source)) {
                source = allowSourcesList.get(0);
            }
            if (!allowSourcesList.contains(source)) {
                throw new ParamErrorException("current support dateSource:" + allowSources);
            }
        }
        SearchSourceEnum requestTypeByDatasource = SearchSourceEnum.getRequestTypeByDatasource(source);
        if (Objects.isNull(requestTypeByDatasource)) {
            throw new ParamErrorException("provid datasource : openEuler, openGauss, openMind, mindSpore,softcenter");
        }
        ThreadLocalCache.putSource(source);
        return true;
    }

    /**
     * @param request      – current HTTP request
     * @param response     – current HTTP response
     * @param handler      – chosen handler to execute, for type and/or instance evaluation
     * @param modelAndView modelAndView – the ModelAndView that the handler returned (can also be null)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * @param request  – current HTTP request
     * @param response – current HTTP response
     * @param handler  – chosen handler to execute, for type and/or instance evaluation
     * @param ex       any exception thrown on handler execution, if any;
     *                 this does not include exceptions that have been handled through an exception resolver
     */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ThreadLocalCache.remove();

    }
}
