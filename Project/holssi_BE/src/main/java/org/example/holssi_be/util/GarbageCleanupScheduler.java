package org.example.holssi_be.util;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.repository.GarbageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class GarbageCleanupScheduler {

    private final GarbageRepository garbageRepository;

    // 스케줄러를 사용하여 수거 완료 후 3일이 지난 쓰레기를 자동으로 삭제
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteCompletedGarbage() {
        List<Garbage> completedGarbages = garbageRepository.findByStatus_CollectionCompleted(true);
        completedGarbages.stream()
                .filter(garbage -> {
                    long diff = new Date().getTime() - garbage.getStatus().getCollectionDate().getTime();
                    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) >= 3;
                })
                .forEach(garbageRepository::delete);
    }
}
