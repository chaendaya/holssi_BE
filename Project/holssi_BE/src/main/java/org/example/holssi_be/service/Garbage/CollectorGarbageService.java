package org.example.holssi_be.service.Garbage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.*;
import org.example.holssi_be.entity.domain.*;
import org.example.holssi_be.exception.InvalidException;
import org.example.holssi_be.exception.NotCollectorException;
import org.example.holssi_be.exception.ResourceNotFoundException;
import org.example.holssi_be.repository.CollectorLocationRepository;
import org.example.holssi_be.repository.CollectorRepository;
import org.example.holssi_be.repository.GarbageRepository;
import org.example.holssi_be.util.GeocodingUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;

@Service
@RequiredArgsConstructor
public class CollectorGarbageService {

    private final GarbageRepository garbageRepository;
    private final GeocodingUtil geocodingUtil;
    private final CollectorLocationRepository collectorLocationRepository;

    // 매칭 대기 중인 쓰레기 리스트 조회 - Collector
    public List<GarbageInfoDTO> getWaitingGarbages(Member collector) {
        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

        String location = collector.getCollector().getLocation();

        return garbageRepository.findByStatus_MatchedAndLocationContaining(false, location)
                .stream()
                .map(this::convertToGarbageInfoDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // 쓰레기 수거 날짜 등록 및 매칭 완료 - Collector
    public void acceptGarbage(Long garbageId, AcceptGarbageDTO acceptGarbageDTO, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

        // 수거자의 지역을 가져온다
        String collectorLocation = collector.getCollector().getLocation();

        // 현재 쓰레기가 수거자의 지역에 맞는지 확인
        if (!garbage.getLocation().contains(collectorLocation)) {
            throw new InvalidException("The garbage location does not match collector's location.");
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
        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

        Collectors collectorEntity = collector.getCollector();

        return garbageRepository.findByCollectorAndStatus_Matched(collectorEntity, true)
                .stream()
                .filter(garbage -> !garbage.getStatus().isStartCollection() && !garbage.getStatus().isCollectionCompleted())
                .map(this::convertToGarbageInfoDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    // 개별 쓰레기 정보 조회
    public GarbageDetailsDTO getGarbageDetails(Long garbageId, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

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
        ComponentDTO organic = new ComponentDTO();
        ComponentDTO non_organic = new ComponentDTO();

        organic.setRP(garbage.getOrganicWeight() * 60);
        organic.setBreat(garbage.getOrganicWeight());

        non_organic.setRP(garbage.getNon_organicWeight() * 80);
        non_organic.setBreat(garbage.getNon_organicWeight());

        GarbageDetailsDTO dto = new GarbageDetailsDTO();
        dto.setOrganic(organic);
        dto.setNon_organic(non_organic);
        dto.setTotalWeight(garbage.getTotalWeight());
        dto.setTotalValue(garbage.getTotalValue());
        return dto;
    }

    // 개별 쓰레기 수거 시작
    public GarbageLocationDTO startCollection(Long garbageId, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

        Collectors collectorEntity = collector.getCollector();
        garbage.setCollector(collectorEntity);
        GarbageStatus status = garbage.getStatus();
        status.setStartCollection(true);

        garbageRepository.save(garbage);

        GarbageLocationDTO dto = new GarbageLocationDTO();
        dto.setGarbageId(garbage.getId());
        dto.setLocation(garbage.getLocation());

        double[] coordinates = geocodingUtil.getCoordinates(garbage.getLocation());
        dto.setLatitude(coordinates[0]);
        dto.setLongitude(coordinates[1]);

        return dto;
    }

    @Transactional
    // 개별 쓰레기 수거 완료
    public void completeCollection(Long garbageId, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

        GarbageStatus status = garbage.getStatus();

        if (status.isMatched() && status.isStartCollection()) {

            Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            status.setCollectionDate(currentDate); // 현재 시간을 CollectionDate 로 설정
            status.setStartCollection(false);
            status.setCollectionCompleted(true);

            Users user = garbage.getUser();

            double currentTotalRp = user.getTotalRp();
            double totalValue = garbage.getTotalValue();

            currentTotalRp += totalValue;
            user.setTotalRp(currentTotalRp);

            garbageRepository.save(garbage);

            // collectorLocation 에서 해당 수거관 위치값 제거
            collectorLocationRepository.deleteByGarbageId(garbageId);
        }
    }

    // 프론트 -> 수거인 위치 저장
    public void updateCollectorLocation(Long garbageId, Member collector, Double latitude, Double longitude) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (collector == null || !collector.getRole().equals("collector")) {
            throw new NotCollectorException();
        }

        CollectorLocation location = garbage.getCollectorLocation();

        if (location == null) {
            location = new CollectorLocation();
            location.setGarbage(garbage);
            location.setCollector(garbage.getCollector());
            location.setUser(garbage.getUser());
        }

        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTimestamp(LocalDateTime.now());

        collectorLocationRepository.save(location);
    }
}
