package org.example.holssi_be.service;

import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Users user) {
        // 비밀번호 인코딩
        user.getMember().setPassword(passwordEncoder.encode(user.getMember().getPassword()));
        memberRepository.save(user.getMember());
        userRepository.save(user);
    }
}