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

@Component
@RequiredArgsConstructor
public class JwtAuthEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
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
        ResponseDTO responseDTO = new ResponseDTO(false, null, "Access is denied");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}
