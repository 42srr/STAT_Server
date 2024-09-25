package ggs.srr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 접속 허용
        registry.addMapping("/**")
                // 모든 도메인 접속 허용
                .allowedOrigins("*")
                // GET, POST 방식 요청 허용
                .allowedMethods("GET", "POST");
    }
}
