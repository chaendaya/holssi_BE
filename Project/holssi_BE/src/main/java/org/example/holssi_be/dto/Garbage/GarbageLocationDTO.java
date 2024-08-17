package org.example.holssi_be.dto.Garbage;

import lombok.Data;

@Data
public class GarbageLocationDTO {
    private Long garbageId;
    private String location;
    private double latitude;
    private double longitude;
}