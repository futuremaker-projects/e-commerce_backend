package com.ecommerce.support.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                                          // 모든 경로에 대해
                .allowedOrigins("http://localhost:3000", "http://localhost:5173")      // 허용할 Origin
                .allowedMethods("GET", "POST", "PUT", "DELETE")                        // 허용할 HTTP Method
                .allowedHeaders("*")                                                   // 모든 헤더 허용
                .allowCredentials(true)                                                // 쿠키/인증 정보 포함 허용 시 필수
                .maxAge(3600);                                                         // Preflight 요청 캐싱 시간 (1시간)
    }

}
