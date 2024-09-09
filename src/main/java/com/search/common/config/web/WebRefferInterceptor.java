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

import com.search.common.constant.SearchConstant;
import com.search.common.thread.ThreadLocalCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class WebRefferInterceptor implements HandlerInterceptor {

    /**
     * Referer pass domain.
     */
    private String allowDomains;

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
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String referer = request.getHeader("Referer");
        if (allowDomains == null || allowDomains.length() == 0) {
            return true;
        }
        String[] domains = allowDomains.split(";");
        boolean checkReferer = checkDomain(domains, referer);
        if (!checkReferer) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

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

    /**
     * check header.
     *
     * @param domains configdomain.
     * @param input   input.
     * @return boolean.
     */
    private boolean checkDomain(String[] domains, String input) {
        if (StringUtils.isBlank(input)) {
            return true;
        }

        String domainToCheck = extractDomainFromUrl(input);

        for (String domain : domains) {
            if (domainToCheck.equals(domain)) {
                return true;
            }
        }

        return false;
    }

    /**
     * check url.
     *
     * @param url url.
     * @return String.
     */

    private String extractDomainFromUrl(String url) {
        String domain = url;

        if (url.startsWith(SearchConstant.HTTPS_PREFIX)) {
            domain = url.substring(SearchConstant.HTTPS_PREFIX.length());
        } else {
            return "";
        }

        int endIndex = domain.indexOf("/");
        if (endIndex != -1) {
            domain = domain.substring(0, endIndex);
        }

        return domain;
    }

    /**
     * constructor.
     *
     * @param allowDomains configdomain.
     */
    public WebRefferInterceptor(String allowDomains) {
        this.allowDomains = allowDomains;
    }
}
