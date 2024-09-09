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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
     * sourceInterceptor set.
     *
     * @return WebSourceInterceptor
     */
    @Bean
    public WebSourceInterceptor sourceInterceptor() {
        return new WebSourceInterceptor(allowsource);
    }

    /**
     * refferInterceptor set.
     *
     * @return WebRefferInterceptor
     */
    @Bean
    public WebRefferInterceptor refferInterceptor() {
        return new WebRefferInterceptor(allowreferers);
    }

    /**
     * 拦截器.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(refferInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(sourceInterceptor()).addPathPatterns("/**");
    }
}
