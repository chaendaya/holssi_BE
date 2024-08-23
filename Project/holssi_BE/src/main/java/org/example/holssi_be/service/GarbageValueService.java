package org.example.holssi_be.service;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.Garbage.GarbageValueDTO;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.entity.domain.GarbageValue;
import org.example.holssi_be.exception.IllegalException;
import org.example.holssi_be.exception.NotAdminException;
import org.example.holssi_be.repository.GarbageValueRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GarbageValueService {

    private final GarbageValueRepository garbageValueRepository;

    public GarbageValue getValue() {
        return garbageValueRepository.findById(1L)
                .orElseGet(() -> {
                    // Handle case when no row with id=1 is found
                    GarbageValue garbageValue = new GarbageValue();
                    garbageValue.setId(1L);
                    return garbageValueRepository.save(garbageValue);
                });
    }

    public GarbageValue updateValue(Member admin, GarbageValueDTO garbageValueDTO) {
        if (admin == null || !admin.getRole().equals("admin")) {
            throw new NotAdminException();
        }

        GarbageValue existingGarbageValue = garbageValueRepository.findById(1L)
                .orElseThrow(() -> new IllegalException("No existing value found with id=1."));

        existingGarbageValue.setOrganic(garbageValueDTO.getOrganicValue());
        existingGarbageValue.setNon_organic(garbageValueDTO.getNon_organicValue());
        return garbageValueRepository.save(existingGarbageValue);
    }
}
