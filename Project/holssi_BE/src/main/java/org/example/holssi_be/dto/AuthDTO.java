package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {

    @NotEmpty(message = "Primary key cannot be empty")
    private String primaryKey;

    @NotEmpty(message = "Identifier cannot be empty")
    private String identifier;

    private String code;

    // 기본 생성자
    public AuthDTO() {}
}
