package com.example.UA.filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<loginFilter> loginFilter() {
        FilterRegistrationBean<loginFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new loginFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
