package org.example.holssi_be.service;

import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.repository.CollectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectorService {

    @Autowired
    private CollectorRepository collectorRepository;

    public Collectors save(Collectors collector) {
        return collectorRepository.save(collector);
    }
}
