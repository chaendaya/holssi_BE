package org.example.holssi_be.config;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.interceptor.AuthTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // 특정 경로에 대해 사용자 정보를 로드하고 요청 속성에 추가
    // HttpServletRequest request 인자로 전달
    // 해당 컨트롤러에서 Member member = (Member) request.getAttribute("member"); 사용 가능
    private final AuthTokenInterceptor authTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenInterceptor)
                .addPathPatterns("/api/garbages/**", "/api/home", "/api/user/**", "/api/collector/garbageId", "/api/admin/status", "/api/admin/updateValue") // 요청을 보낸 사용자의 member 속성이 필요한 경로
                .excludePathPatterns("/", "/api/auth/**", "/api/login", "/api/admin/create", "/h2-console",
                        "/h2-console/**", "/api/temp/**", "/api/refresh-token"); // 요청을 보낸 사용자의 member 속성이 필요 없는 경로
    }
}