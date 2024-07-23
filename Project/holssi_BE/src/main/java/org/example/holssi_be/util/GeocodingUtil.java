package org.example.holssi_be.util;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.service.GeocodingService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeocodingUtil {

    private final GeocodingService geocodingService;

    public double[] getCoordinates(String location) {
        return geocodingService.getCoordinates(location);
    }
}
