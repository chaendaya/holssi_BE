package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {

    @NotBlank(message = "role is required")
    private String role;

    @NotBlank(message = "tempKey is required")
    private String tempKey;

    private String code;

    @NotBlank
    private String email;
}
