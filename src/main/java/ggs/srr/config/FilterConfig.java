package ggs.srr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.security.authentication.AuthenticationManager;
import ggs.srr.security.authentication.filter.AuthenticationFilter;
import ggs.srr.security.authorization.AuthorizationFilter;
import ggs.srr.security.cors.CorsFilter;
import ggs.srr.security.jwt.JwtUtils;
import ggs.srr.security.login.filter.LoginFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CorsFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> loginFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginFilter(authenticationManager, objectMapper));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/login/oauth2/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> authenticationFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new AuthenticationFilter(jwtUtils, objectMapper));
        filterFilterRegistrationBean.setOrder(3);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> authorizationFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthorizationFilter(objectMapper));
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
