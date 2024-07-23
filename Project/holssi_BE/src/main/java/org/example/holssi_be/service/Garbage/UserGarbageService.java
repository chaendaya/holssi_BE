package org.example.holssi_be.service.Garbage;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.dto.Garbage.RegisteredGarbageDTO;
import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.entity.domain.GarbageStatus;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.exception.ResourceNotFoundException;
import org.example.holssi_be.exception.UnauthorizedAccessException;
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
        Garbage garbage = create(registerGarbageDTO, member);
        garbageRepository.save(garbage);
    }

    // 쓰레기 등록 DTO -> Entity
    private Garbage create(RegisterGarbageDTO dto, Member member) {
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
    public List<RegisteredGarbageDTO> getRegisteredGarbages(Long userId) {
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
    public double getTotalRp(Long userId, Member user) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!user.getRole().equals("user")) {
            throw new UnauthorizedAccessException("Member is not a user.");
        }

        return users.getTotalRp();
    }
}
