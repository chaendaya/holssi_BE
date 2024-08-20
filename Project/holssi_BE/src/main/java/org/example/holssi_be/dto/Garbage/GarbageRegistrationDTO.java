package org.example.holssi_be.dto.Garbage;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GarbageRegistrationDTO {

    @NotNull(message = "organicWeight is required")
    private Double organicWeight;

    @NotNull(message = "non_organicWeight is required")
    private Double non_organicWeight;
}
