package org.example.holssi_be.repository;


import jakarta.transaction.Transactional;
import org.example.holssi_be.dto.LocationDTO;
import org.example.holssi_be.entity.domain.CollectorLocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CollectorLocationRepository extends JpaRepository<CollectorLocation, Long> {

    @Query("SELECT new org.example.holssi_be.dto.LocationDTO(c.latitude, c.longitude) " +
            "FROM CollectorLocation c WHERE c.garbage.id = :garbageId")
    LocationDTO findLocationById(Long garbageId);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CollectorLocation c WHERE c.garbage.id = :garbageId")
    void deleteByGarbageId(Long garbageId);

}

