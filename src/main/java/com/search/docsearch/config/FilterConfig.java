package com.search.docsearch.config;


import com.search.docsearch.filter.RequestRefferFilter;
import com.search.docsearch.filter.TokenFilter;
import com.search.docsearch.filter.ResponceHeaderFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 加载所有的filter并指定filter顺序
 */
@Slf4j
@Configuration
public class FilterConfig {
    /**
     * Referer pass domain.
     */
    @Value("${header.referers:}")
    private String allowDomains;

    /**
     * 编码过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean encodingFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        filterRegistrationBean.setFilter(characterEncodingFilter);
        // 顺序
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    /**
     * 响应头拦截器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean headerFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        TokenFilter tokenFilter = new TokenFilter();
        filterRegistrationBean.setFilter(tokenFilter);
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * Reffer拦截器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean requestRefferFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        RequestRefferFilter requestRefferFilter = new RequestRefferFilter(allowDomains);
        filterRegistrationBean.setFilter(requestRefferFilter);
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 响应头拦截器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean crossFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        ResponceHeaderFilter responceHeaderFilter = new ResponceHeaderFilter();
        filterRegistrationBean.setFilter(responceHeaderFilter);
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
