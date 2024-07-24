package org.example.holssi_be.dto.Garbage;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterGarbageDTO {

    @NotBlank(message = "organicWeight is required")
    private Double organicWeight;

    @NotBlank(message = "non_organicWeight is required")
    private Double non_organicWeight;
}
