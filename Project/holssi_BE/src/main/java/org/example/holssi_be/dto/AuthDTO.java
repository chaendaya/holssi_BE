package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {

    @NotEmpty(message = "Role cannot be empty")
    private String role;

    @NotEmpty(message = "Primary key cannot be empty")
    private String tempKey;

    private String code;

    // 기본 생성자
    public AuthDTO() {}
}
