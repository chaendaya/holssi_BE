package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IdCheckRequestDTO {

    @NotBlank(message = "email(Id) is required")
    private String email;

}

