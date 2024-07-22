package org.example.holssi_be.util;

import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.repository.GarbageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class GarbageUpdateScheduler {

    @Autowired
    private GarbageRepository garbageRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateDaysSinceRegistration() {
        List<Garbage> allGarbages = garbageRepository.findAll();
        for (Garbage garbage : allGarbages) {
            long daysSinceRegistration = ChronoUnit.DAYS.between(
                    garbage.getRegistrationDate().toInstant(),
                    Instant.now()
            );
            garbage.setDaysSinceRegistration(daysSinceRegistration);
            garbageRepository.save(garbage);
        }
    }
}