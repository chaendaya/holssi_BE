package org.example.holssi_be.service.Garbage;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.dto.Garbage.RegisteredGarbageDTO;
import org.example.holssi_be.entity.domain.*;
import org.example.holssi_be.exception.NotUserException;
import org.example.holssi_be.exception.ResourceNotFoundException;
import org.example.holssi_be.repository.GarbageRepository;
import org.example.holssi_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGarbageService {

    private final GarbageRepository garbageRepository;
    private final UserRepository userRepository;

    // 쓰레기 등록 - User
    public void registerGarbage(RegisterGarbageDTO registerGarbageDTO, Member member) {
        if (member == null || !member.getRole().equals("user")) {
            throw new NotUserException();
        }

        Garbage garbage = create(registerGarbageDTO, member);
        garbageRepository.save(garbage);
    }

    // 쓰레기 등록 DTO -> Entity
    private Garbage create(RegisterGarbageDTO dto, Member member) {
        if (member == null || !member.getRole().equals("user")) {
            throw new NotUserException();
        }

        double organicWeight = dto.getOrganicWeight();
        double non_organicWeight = dto.getNon_organicWeight();
        double totalValue = 60 * organicWeight + 80 * non_organicWeight;

        Garbage garbage = new Garbage();
        garbage.setUser(member.getUser());
        garbage.setOrganicWeight(organicWeight);
        garbage.setNon_organicWeight(non_organicWeight);
        garbage.setTotalWeight(organicWeight + non_organicWeight);
        garbage.setTotalValue(totalValue);
        garbage.setLocation(member.getUser().getLocation());

        GarbageStatus status = new GarbageStatus();
        status.setGarbage(garbage);
        garbage.setStatus(status);

        return garbage;
    }

    // 등록한 쓰레기 중 수거인 매칭된 쓰레기 리스트 조회 - User
    public List<RegisteredGarbageDTO> getRegisteredGarbages(Member user) {
        // 사용자 객체가 null 이거나 역할이 "user"가 아닌 경우 예외를 던짐
        if (user == null || !user.getRole().equals("user")) {
            throw new NotUserException();
        }

        // 사용자 ID가 null 인 경우 예외를 던짐
        Long userId = user.getId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID is null");
        }

        // 사용자 ID로 데이터베이스에서 사용자를 조회, 없으면 예외를 던짐
        Users foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return garbageRepository.findByStatus_MatchedAndUserId(true, userId)
                .stream()
                .map(this::convertToRegisteredGarbageDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    private RegisteredGarbageDTO convertToRegisteredGarbageDTO(Garbage garbage) {
        RegisteredGarbageDTO dto = new RegisteredGarbageDTO();
        dto.setGarbageId(garbage.getId());
        dto.setMatched(garbage.getStatus().isMatched());
        dto.setCollectorName(garbage.getCollector().getMember().getName());
        dto.setCollectionDayOfWeek(getDayOfWeek(garbage.getStatus().getCollectionDate()));
        dto.setCollectionStatus(determineCollectionStatus(garbage));

        return dto;
    }

    // 날짜를 요일로 변환하는 메서드
    public String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "일요일";
            case Calendar.MONDAY:
                return "월요일";
            case Calendar.TUESDAY:
                return "화요일";
            case Calendar.WEDNESDAY:
                return "수요일";
            case Calendar.THURSDAY:
                return "목요일";
            case Calendar.FRIDAY:
                return "금요일";
            case Calendar.SATURDAY:
                return "토요일";
            default:
                return "";
        }
    }

    // 수거 상태를 결정하는 메서드
    private String determineCollectionStatus(Garbage garbage) {
        GarbageStatus status = garbage.getStatus();
        if (status.isMatched()) {
            if (status.isStartCollection() && !status.isCollectionCompleted()) {
                return "수거중";
            } else if (!status.isStartCollection() && status.isCollectionCompleted()) {
                return "수거 완료";
            } else if (!status.isStartCollection() && !status.isCollectionCompleted()) {
                return "수거 시작 전";
            } else if (status.isStartCollection() && status.isCollectionCompleted()) {
                throw new IllegalStateException("수거 상태 에러: 수거 시작과 완료가 동시에 참일 수 없습니다.");
            }
        }
        return "매칭 안됨";
    }

    // 저축된 총 RP값 조회
    public double getTotalRp(Member user) {
        // 사용자 객체가 null 이거나 역할이 "user"가 아닌 경우 예외를 던짐
        if (user == null || !user.getRole().equals("user")) {
            throw new NotUserException();
        }

        // 사용자 ID가 null 인 경우 예외를 던짐
        Long userId = user.getId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID is null");
        }

        // 사용자 ID로 데이터베이스에서 사용자를 조회, 없으면 예외를 던짐
        Users foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return foundUser.getTotalRp();
    }

    // 개별 쓰레기 수거인 정보
    public String getCollectorInfo(Long garbageId, Member member) {
        Garbage garbage = garbageRepository.findById(garbageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garbage not found with ID: " + garbageId));

        // 사용자 객체가 null 이거나 역할이 "user"가 아닌 경우 예외를 던짐
        if (member == null || (!member.getRole().equals("user") && !member.getRole().equals("admin"))) {
            throw new NotUserException();
        }

        Collectors collector = garbage.getCollector();
        Member members = collector.getMember();

        return members.getName();
    }
}


