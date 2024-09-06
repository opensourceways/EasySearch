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


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加载所有的filter并指定filter顺序
 */
@Slf4j
@Configuration
public class FilterConfig {
    /**
     * port.
     */
    @Value("${header.source:}")
    private String allowsource;


    /**
     * Referer pass referers.
     */
    @Value("${header.referers:}")
    private String allowreferers;

    /**
     * 数据源filter,设置当前请求的数据源
     *
     * @return FilterRegistrationBean .
     */
    @Bean
    public FilterRegistrationBean dataSourceFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DataSourceFilter dataSourceFilter = new DataSourceFilter(allowsource);
        filterRegistrationBean.setFilter(dataSourceFilter);
        // 顺序
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    /**
     * 请求头拦截器.
     *
     * @return FilterRegistrationBean .
     */
    @Bean
    public FilterRegistrationBean requestHeaderFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        RequestHeaderFilter requestHeaderFilter = new RequestHeaderFilter(allowreferers);
        filterRegistrationBean.setFilter(requestHeaderFilter);
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
