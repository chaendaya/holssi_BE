package org.example.holssi_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDTO {

    private Double latitude;
    private Double longitude;
    private Integer daysSinceRegistration;

    public LocationDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.daysSinceRegistration = -1;
    }

}