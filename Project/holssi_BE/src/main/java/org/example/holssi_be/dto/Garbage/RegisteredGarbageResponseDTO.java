package org.example.holssi_be.dto.Garbage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RegisteredGarbageResponseDTO {
    private List<RegisteredGarbageDTO> data;
    private int nextPage;

    @JsonProperty("isLast")
    private boolean isLast;
}
