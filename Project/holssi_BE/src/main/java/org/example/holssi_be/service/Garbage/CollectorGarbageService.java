package org.example.holssi_be.service.Garbage;

import org.example.holssi_be.dto.Garbage.AcceptGarbageDTO;
import org.example.holssi_be.dto.Garbage.GarbageDetailsDTO;
import org.example.holssi_be.dto.Garbage.GarbageInfoDTO;
import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.exception.ResourceNotFoundException;
import org.example.holssi_be.repository.GarbageRepository;
import org.example.holssi_be.util.GeocodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CollectorGarbageService {

    @Autowired
    private GarbageRepository garbageRepository;

    @Autowired
    private GeocodingUtil geocodingUtil;

    // 매칭 대기 중인 쓰레기 리스트 조회 - Collector
    public List<GarbageInfoDTO> getWaitingGarbages(String collectorLocation) {
        return garbageRepository.findByMatchedAndLocationContaining(false, collectorLocation)
                .stream()
                .map(this::convertToGarbageInfoDTO)
                .collect(Collectors.toList());
    }

    // 쓰레기 수거 날짜 등록 및 매칭 완료 - Collector
    public void acceptGarbage(Long garbageId, AcceptGarbageDTO acceptGarbageDTO, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        garbage.setCollector(collector.getCollector());
        garbage.setCollectionDate(acceptGarbageDTO.getCollectionDate());
        garbage.setMatched(true);
        garbage.setStartCollection(false);

        garbageRepository.save(garbage);
    }

    // 수거인 자신이 수락한 쓰레기 리스트 조회 - Collector
    public List<GarbageInfoDTO> getAcceptedGarbages(Member collector) {
        return garbageRepository.findByCollectorAndMatched(collector.getCollector(), true)
                .stream()
                .map(this::convertToGarbageInfoDTO)
                .collect(Collectors.toList());
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
        dto.setMatched(garbage.isMatched());

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
