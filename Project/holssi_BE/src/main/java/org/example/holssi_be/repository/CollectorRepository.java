package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectorRepository extends JpaRepository<Collectors, Long> {
    Optional<Collectors> findByMemberEmail(String email);
}
