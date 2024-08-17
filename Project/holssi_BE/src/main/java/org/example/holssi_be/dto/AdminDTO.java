package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "admin's email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "phone is required")
    private String phone;
}
