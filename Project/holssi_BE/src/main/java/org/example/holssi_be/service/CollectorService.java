package org.example.holssi_be.service;

import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.repository.CollectorRepository;
import org.example.holssi_be.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CollectorService {

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Collectors collector) {
        // 비밀번호 인코딩
        collector.getMember().setPassword(passwordEncoder.encode(collector.getMember().getPassword()));
        memberRepository.save(collector.getMember());
        collectorRepository.save(collector);
    }

    public Collectors findById(Long id) {
        return collectorRepository.findById(id).orElse(null);
    }
}
