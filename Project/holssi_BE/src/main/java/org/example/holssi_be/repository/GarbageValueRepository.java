package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.GarbageValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarbageValueRepository extends JpaRepository<GarbageValue, Long> {
}
