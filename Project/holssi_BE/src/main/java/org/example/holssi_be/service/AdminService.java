package org.example.holssi_be.service;

import org.example.holssi_be.dto.AdminDTO;
import org.example.holssi_be.entity.domain.Admins;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.repository.AdminRepository;
import org.example.holssi_be.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createAdmin(AdminDTO adminDTO) {
        Member member = new Member();
        member.setEmail(adminDTO.getAdminEmail());
        member.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        member.setName(adminDTO.getName());
        member.setPhone(adminDTO.getPhone());
        member.setRole("admin");
        memberRepository.save(member);

        Admins admin = new Admins();
        admin.setMember(member);
        adminRepository.save(admin);
    }
}
