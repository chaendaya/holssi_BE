package org.example.holssi_be.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RatingRequestDTO {

    @NotNull(message = "score is required")
    private Integer score;
}
