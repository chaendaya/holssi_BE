package org.example.holssi_be.dto.auth;

import lombok.Data;

@Data
public class AuthDTO {
    private String identifier;  // 이메일 또는 전화번호
    private String code;
}
