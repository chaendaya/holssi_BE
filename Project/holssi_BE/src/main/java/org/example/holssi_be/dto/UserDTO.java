package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "user's email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "location is required")
    private String location;

    @NotBlank(message = "account is required")
    private String account;

    @NotBlank(message = "bank is required")
    private String bank;
}
