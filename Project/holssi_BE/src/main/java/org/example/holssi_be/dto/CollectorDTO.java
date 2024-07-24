package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CollectorDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "collectorEmail is required")
    private String collectorEmail;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "phone is required")
    private String phone;

    @NotBlank(message = "location is required")
    private String location;
}
