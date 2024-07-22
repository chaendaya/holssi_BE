package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.entity.domain.Garbage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GarbageRepository extends JpaRepository<Garbage, Long> {
    List<Garbage> findByMatchedAndLocationContaining(boolean matched, String location);
    List<Garbage> findByCollectorAndMatched(Collectors collector, boolean matched);

}
