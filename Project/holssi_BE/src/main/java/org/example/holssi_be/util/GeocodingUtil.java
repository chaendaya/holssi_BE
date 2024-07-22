package org.example.holssi_be.util;

import org.example.holssi_be.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeocodingUtil {

    @Autowired
    private GeocodingService geocodingService;

    public double[] getCoordinates(String location) {
        return geocodingService.getCoordinates(location);
    }
}
