package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.GarbageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarbageStatusRepository extends JpaRepository<GarbageStatus, Long> {
    List<GarbageStatus> findByCollectorIdAndStartCollection(Long collectorId, boolean startCollection);
}
