package org.example.holssi_be.config;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.interceptor.AuthTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
                .addPathPatterns("/api/garbages/**", "/api/home", "/api/user/**", "/api/admin/status") // 요청을 보낸 사용자의 member 속성이 필요한 경로
                .excludePathPatterns("/", "/api/auth/**", "/api/login", "/api/admin/create", "/h2-console",
                        "/h2-console/**", "/api/temp/**"); // 요청을 보낸 사용자의 member 속성이 필요 없는 경로
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS를 설정
                .allowedOrigins("http://ec2-43-202-58-157.ap-northeast-2.compute.amazonaws.com:8090") // 허용할 도메인 (포트 번호 포함)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 쿠키 허용 여부
    }
}