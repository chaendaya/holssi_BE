package org.example.holssi_be.service.Garbage;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.GarbageInfoDTO;
import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.NotCollectorException;
import org.example.holssi_be.repository.GarbageRepository;
import org.example.holssi_be.util.GeocodingUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AdminGarbageService {

    private final GarbageRepository garbageRepository;
    private final GeocodingUtil geocodingUtil;

    public List<GarbageInfoDTO> getAllWaitingGarbages(Member admin) {
        if (admin == null || !admin.getRole().equals("admin")) {
            throw new NotCollectorException();
        }

        return garbageRepository.findByStatus_Matched(false)
                .stream()
                .map(this::convertToGarbageInfoDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    private GarbageInfoDTO convertToGarbageInfoDTO(Garbage garbage) {
        GarbageInfoDTO dto = new GarbageInfoDTO();
        dto.setGarbageId(garbage.getId());
        dto.setLocation(garbage.getLocation());
        dto.setMatched(garbage.getStatus().isMatched());

        double[] coordinates = geocodingUtil.getCoordinates(garbage.getLocation());
        dto.setLatitude(coordinates[0]);
        dto.setLongitude(coordinates[1]);
        dto.setDaysSinceRegistration(calculateDaysSinceRegistration(garbage.getRegistrationDate()));

        return dto;
    }

    private long calculateDaysSinceRegistration(Date registrationDate) {
        long diff = new Date().getTime() - registrationDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
