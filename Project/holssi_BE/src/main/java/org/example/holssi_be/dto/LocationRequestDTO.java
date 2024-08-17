package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationRequestDTO {

    @NotBlank(message = "latitude is required")
    private Double latitude;

    @NotBlank(message = "longitude is required")
    private Double longitude;
}
