package org.example.holssi_be.dto.Garbage;

import lombok.Data;

@Data
public class GarbageDetailsDTO {
    private Double organicWeight;
    private Double non_organicWeight;
    private Double totalWeight;
    private Double totalValue;
}