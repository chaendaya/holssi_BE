package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.CollectorLocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CollectorLocationRepository extends JpaRepository<CollectorLocation, Long> {

    CollectorLocation findByCollectorId(Long collectorId);

}

