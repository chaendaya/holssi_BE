package org.example.holssi_be.service;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.repository.CollectorRepository;
import org.example.holssi_be.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final CollectorRepository collectorRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void save(Collectors collector) {
        // 비밀번호 인코딩
        collector.getMember().setPassword(passwordEncoder.encode(collector.getMember().getPassword()));
        memberRepository.save(collector.getMember());
        collectorRepository.save(collector);
    }
}
