package org.example.holssi_be.service.Garbage;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.AcceptGarbageDTO;
import org.example.holssi_be.dto.Garbage.GarbageDetailsDTO;
import org.example.holssi_be.dto.Garbage.GarbageInfoDTO;
import org.example.holssi_be.entity.domain.*;
import org.example.holssi_be.exception.ResourceNotFoundException;
import org.example.holssi_be.exception.UnauthorizedAccessException;
import org.example.holssi_be.repository.GarbageRepository;
import org.example.holssi_be.util.GeocodingUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    // 개별 쓰레기 수거 시작
    public void startCollection(Long garbageId, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (!collector.getRole().equals("collector")) {
            throw new UnauthorizedAccessException("Member is not a collector.");
        }

        Collectors collectorEntity = collector.getCollector();
        garbage.setCollector(collectorEntity);
        GarbageStatus status = garbage.getStatus();
        status.setStartCollection(true);

        garbageRepository.save(garbage);
    }



    // 개별 쓰레기 수거 완료
    public void completeCollection(Long garbageId, Member collector) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        if (!collector.getRole().equals("collector")) {
            throw new UnauthorizedAccessException("Member is not a collector.");
        }

        // collector가 쓰레기 수락할 때 garbage의 collector로 set되도록 해놔서 또 setCollector 하진 않아도 될 것 같아!
        Collectors collectorEntity = collector.getCollector();
        garbage.setCollector(collectorEntity);

        GarbageStatus status = garbage.getStatus();

        if (status.isMatched() && status.isStartCollection()) {

            Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
            status.setCollectionDate(currentDate); // 현재 시간을 CollectionDate 로 설정
            status.setStartCollection(false);
            status.setCollectionCompleted(true);

            /* 우리가 필요한 것!
                1. 사용자의 totalRp, 2. 쓰레기의 totalValue

                계획 : 사용자의 totalRp 컬럼 값을 가져오고(get),
                      쓰레기의 totalValue 컬럼 값을 가져온다(get)
                      가져온 totalRp 값에 totalValue 값을 더한다.
                      덧셈 연산한 값을 사용자의 totalRp 컬럼에 설정해준다(set)

                사용자의 totalRp를 어떻게 구할까?

                지금 우리는 garbageId로 garbage 저장소에서 객체를 찾아왔고
                e.g. Garbage garbage = garbageRepository.findById(garbageId)

                해당 쓰레기에 대한 모든 컬럼들과, 몇대몇 관계로 매핑된 엔티티의 객체들을 얻을 수 있었지!
                e.g. GarbageStatus status = garbage.getStatus();
                     이렇게 Garbage 와 연관된 엔티티 GarbageStatus 의 객체를 가져왔지 소형이가!

                    * 참고로 위에서 내가 또 setCollector 하진 않아도 될것 같다고한지는
                       이제 위의 acceptGarbage 함수를 봐보면 이해할 수 있을거양

                당장 garbage.get 만 쳐도 너가 가져올 수 있는 값들이 주르륵 뜰꺼야
                그 중 사용자 객체를 가져와보자!
            */

            // Users user =  ???

            /*
                이제 user.get 을 쳐보고 우리가 필요한 값을 가져올 수 있는지 확인해보자!
                변수 명을 currentTotalRp 라고 하면 그 타입은 어떻게 해야할까?
                힌트는 entity.domain 패키지에 있는 Users 에서 확인하기~
            */

            // ??? currentTotalRp = ???

            /*
                이제 나머지 필요한 값도 비슷하게 가져올 수 있겠지?
                값들 가져와서 바로 연산해도 되고, 하나하나 변수 선언해서 사용해도되고.. 뭐든 소형이와 내가 코드 읽기 편한쪽으로!

                마지막은 저장인데! set 만 하면 데이터베이스에 반영 안되기 때문에
                Spring Data JPA 의 JpaRepository 인터페이스에서 제공하는 기본 메서드 save 를 사용할거야 (내장되어있음!)

                우리 프로젝트 폴더 구조를 봐보면 repository 가 있을 거야!
                코드 파일 앞에 초록색으로 I 라고 써져있징?
                이게 JpaRepository 인터페이스를 extends 해서 우리 엔티티에 맞게 만든 인터페이스야
                이 인터페이스를 통해서 해당 엔티티에 대해 find 도 하고 save 도 편하게 할 수 있는 거징

                다시 돌아와서
                지금 garbage 로부터 계속 값을 가져온거잖아? garbage 에서 user 엔티티 가져오고 상태도 가져오고..
                garbageRepository.save(garbage)를 호출하면
                Garbage 엔티티뿐만 아니라 매핑으로 연관된 엔티티들의 변경사항도 저장되기 때문에
                여기서는 아래 마지막 코드만 유지하면 되겠다 :D
             */
            garbageRepository.save(garbage);

        }
    }
}

