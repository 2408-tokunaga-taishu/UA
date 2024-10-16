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
        // 今回はloginページはフィルターをかけないようにloginFilter.javaで記述しているので
        // ワイルドカード使用しても大丈夫
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<ManageFilter> manageFilter() {
        FilterRegistrationBean<ManageFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new ManageFilter());
        //  管理者権限が必要なURL
        bean.addUrlPatterns("/editAccount/*");
        bean.addUrlPatterns("/newAccount");
        bean.addUrlPatterns("/accountManage");
        bean.setOrder(2);
        return bean;
    }
}
