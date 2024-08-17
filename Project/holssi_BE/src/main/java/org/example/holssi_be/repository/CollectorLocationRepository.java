package org.example.holssi_be.repository;

import org.example.holssi_be.dto.LocationDto;
import org.example.holssi_be.entity.domain.CollectorLocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CollectorLocationRepository extends JpaRepository<CollectorLocation, Long> {

    @Query("SELECT new org.example.holssi_be.dto.LocationDto(c.latitude, c.longitude) " +
            "FROM CollectorLocation c WHERE c.garbage.id = :garbageId")
    LocationDto findLocatoinById(Long garbageId);
}

