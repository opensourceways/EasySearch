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

package com.search.docsearch.filter;

import com.search.docsearch.constant.Constants;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import java.io.IOException;

/**
 * 请求头拦截器.
 */
@Slf4j
public class RequestRefferFilter implements Filter {
    /**
     * Referer pass domain.
     */
    private String allowDomains;


    /**
     * check header.
     *
     * @param servletRequest  The request.
     * @param servletResponse The response.
     * @param filterChain     The filterChain.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (allowDomains == null || allowDomains.length() == 0) {
            filterChain.doFilter(request, response);
        }
        String referer = request.getHeader("Referer");
        String[] domains = allowDomains.split(";");
        boolean checkReferer = checkDomain(domains, referer);
        if (!checkReferer) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

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

        if (url.startsWith(Constants.HTTPS_PREFIX)) {
            domain = url.substring(Constants.HTTPS_PREFIX.length());
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
    public RequestRefferFilter(String allowDomains) {
        this.allowDomains = allowDomains;
    }
}
