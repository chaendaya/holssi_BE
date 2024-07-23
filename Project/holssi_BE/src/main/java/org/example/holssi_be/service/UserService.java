package org.example.holssi_be.service;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void save(Users user) {
        // 비밀번호 인코딩
        user.getMember().setPassword(passwordEncoder.encode(user.getMember().getPassword()));
        memberRepository.save(user.getMember());
        userRepository.save(user);
    }
}