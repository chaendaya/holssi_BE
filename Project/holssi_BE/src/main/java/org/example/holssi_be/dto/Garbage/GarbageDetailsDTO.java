package org.example.holssi_be.dto.Garbage;

import lombok.Data;

@Data
public class GarbageDetailsDTO {
    private ComponentDTO organic;
    private ComponentDTO non_organic;
    private Double totalWeight;
    private Double totalValue;
    private String status;
}