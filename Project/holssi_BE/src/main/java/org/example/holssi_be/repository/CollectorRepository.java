package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectorRepository extends JpaRepository<Collectors, Long> {
    Collectors findByEmail(String collectorEmail);
    Collectors findByPhone(String phone);
}
