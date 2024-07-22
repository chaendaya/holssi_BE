package org.example.holssi_be.config;

import org.example.holssi_be.interceptor.AuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/api/garbages/**") // 인증이 필요한 경로
                .excludePathPatterns("/api/auth/**", "/api/login", "/api/admin/create",
                        "/h2-console/**", "/api/temp/**", "/api/garbages/totalWeight"); // 인증이 필요 없는 경로
    }
}