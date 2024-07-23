package org.example.holssi_be.service.Garbage;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.AcceptGarbageDTO;
import org.example.holssi_be.dto.Garbage.GarbageDetailsDTO;
import org.example.holssi_be.dto.Garbage.GarbageInfoDTO;
import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.entity.domain.GarbageStatus;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.ResourceNotFoundException;
import org.example.holssi_be.exception.UnauthorizedAccessException;
import org.example.holssi_be.repository.GarbageRepository;
import org.example.holssi_be.util.GeocodingUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CollectorGarbageService {

    private final GarbageRepository garbageRepository;

    private final GeocodingUtil geocodingUtil;

    // 매칭 대기 중인 쓰레기 리스트 조회 - Collector
    public List<GarbageInfoDTO> getWaitingGarbages(String collectorLocation) {
        return garbageRepository.findByStatus_MatchedAndLocationContaining(false, collectorLocation)
                .stream()
                .map(this::convertToGarbageInfoDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // 쓰레기 수거 날짜 등록 및 매칭 완료 - Collector
    public void acceptGarbage(Long garbageId, AcceptGarbageDTO acceptGarbageDTO, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (!collector.getRole().equals("collector")) {
            throw new UnauthorizedAccessException("Member is not a collector.");
        }

        Collectors collectorEntity = collector.getCollector();
        garbage.setCollector(collectorEntity);
        GarbageStatus status = garbage.getStatus();
        status.setCollectionDate(acceptGarbageDTO.getCollectionDate());
        status.setMatched(true);
        status.setStartCollection(false);

        garbageRepository.save(garbage);
    }

    // 수거인 자신이 수락한 쓰레기 리스트 조회 - Collector
    public List<GarbageInfoDTO> getAcceptedGarbages(Member collector) {
        if (!collector.getRole().equals("collector")) {
            throw new UnauthorizedAccessException("Member is not a collector.");
        }

        Collectors collectorEntity = collector.getCollector();
        return garbageRepository.findByCollectorAndStatus_Matched(collectorEntity, true)
                .stream()
                .filter(garbage -> !garbage.getStatus().isStartCollection() && !garbage.getStatus().isCollectionCompleted())
                .map(this::convertToGarbageInfoDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // 개별 쓰레기 정보 조회
    public GarbageDetailsDTO getGarbageDetails(Long garbageId) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));
        return convertToGarbageDetailsDTO(garbage);
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

    private GarbageDetailsDTO convertToGarbageDetailsDTO(Garbage garbage) {
        GarbageDetailsDTO dto = new GarbageDetailsDTO();
        dto.setOrganicWeight(garbage.getOrganicWeight());
        dto.setNon_organicWeight(garbage.getNon_organicWeight());
        dto.setTotalWeight(garbage.getTotalWeight());
        dto.setTotalValue(garbage.getTotalValue());
        return dto;
    }
}
