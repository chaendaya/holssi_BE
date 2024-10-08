package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.entity.domain.Garbage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarbageRepository extends JpaRepository<Garbage, Long> {
    List<Garbage> findByStatus_MatchedAndLocationContaining(boolean matched, String location);
    List<Garbage> findByCollectorAndStatus_Matched(Collectors collector, boolean matched);
    Page<Garbage> findByStatus_MatchedAndUserId(boolean matched, Long userId, Pageable pageable);
    Page<Garbage> findByUserId(Long userId, Pageable pageable);
    List<Garbage> findByStatus_CollectionCompleted(boolean collectionCompleted);
    List<Garbage> findByStatus_Matched(boolean matched);
}
