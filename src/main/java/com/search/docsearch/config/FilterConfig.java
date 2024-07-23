package com.search.docsearch.config;


import com.search.docsearch.filter.ContentTypeFilter;
import com.search.docsearch.filter.CrossFilter;
import lombok.extern.slf4j.Slf4j;
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
     * 编码过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean encodingFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        log.info("characterEncodingFilter....");
        characterEncodingFilter.setEncoding("UTF-8");
        filterRegistrationBean.setFilter(characterEncodingFilter);
        // 顺序
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean headerFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        ContentTypeFilter contentTypeFilter = new ContentTypeFilter();
        log.info("headerFilter.....");
        filterRegistrationBean.setFilter(contentTypeFilter);
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean crossFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CrossFilter crossFilter = new CrossFilter();
        log.info("crossFilter.....");
        filterRegistrationBean.setFilter(crossFilter);
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
