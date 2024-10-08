package org.example.holssi_be.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    // JwtAuthEntryPoint : 인증이 실패했거나 권한이 없는 사용자가 요청했을 때의 진입점을 처리

    private final ObjectMapper objectMapper;
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/",
            "/index.html",
            "/favicon.ico",
            "/api/auth/.*",
            "/api/login",
            "/api/admin/create",
            "/h2-console",
            "/h2-console/.*",
            "/api/temp/.*",
            "/api/garbages/getValue"
    );

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // commence : 요청이 JWT 인증을 요구하지만, 인증이 실패한 경우.

        // JWT 필터를 적용하지 않을 경로 확인
        String requestURI = request.getRequestURI();
        if (shouldExclude(requestURI)) {
            return;
        }

        String message;
        HttpStatus status;

        if (authException.getCause() instanceof ExpiredJwtException) {
            message = "JWT token is expired";
            status = HttpStatus.UNAUTHORIZED;
        } else if (authException.getCause() instanceof MalformedJwtException) {
            message = "JWT token is malformed";
            status = HttpStatus.UNAUTHORIZED;
        } else if (authException.getCause() instanceof UnsupportedJwtException) {
            message = "JWT token is unsupported";
            status = HttpStatus.UNAUTHORIZED;
        } else if (authException.getCause() instanceof SignatureException) {
            message = "JWT signature is invalid";
            status = HttpStatus.UNAUTHORIZED;
        } else {
            message = "Authentication failed: " + authException.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }

        ResponseDTO responseDTO = new ResponseDTO(false, null, message);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // 인증된 사용자가 권한이 없는 리소스에 접근할 때 발생

        // JWT 필터를 적용하지 않을 경로 확인
        String requestURI = request.getRequestURI();
        if (shouldExclude(requestURI)) {
            return;
        }

        ResponseDTO responseDTO = new ResponseDTO(false, null, "Access is denied");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }

    private boolean shouldExclude(String requestURI) {
        // `/` 경로는 반드시 제외되도록 수정
        if ("/".equals(requestURI)) {
            return true;
        }
        return EXCLUDED_PATHS.stream().anyMatch(pattern -> requestURI.matches(pattern));
    }
}
