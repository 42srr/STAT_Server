package ggs.srr.config;

import ggs.srr.filter.AuthorizationFilter;
import ggs.srr.filter.CorsFilter;
import ggs.srr.jwt.JWTExceptionHandler;
import ggs.srr.jwt.JWTFilter;
import ggs.srr.jwt.JWTUtil;
import ggs.srr.oauth.client.ClientManager;
import ggs.srr.oauth.provider.ProviderManager;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    private JWTUtil jwtUtil;
    private JWTExceptionHandler jwtExceptionHandler;

    @Autowired
    public WebConfig(JWTUtil jwtUtil, JWTExceptionHandler jwtExceptionHandler) {
        this.jwtUtil = jwtUtil;
        this.jwtExceptionHandler = jwtExceptionHandler;
    }

    @Bean
    public FilterRegistrationBean<Filter> corsFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CorsFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean<Filter> jwtFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JWTFilter(jwtUtil, jwtExceptionHandler));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> authorizationFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthorizationFilter(jwtUtil));
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
