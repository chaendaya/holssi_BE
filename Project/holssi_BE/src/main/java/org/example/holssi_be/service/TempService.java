package org.example.holssi_be.service;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.dto.CollectorDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.repository.CollectorRepository;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempService {

    private final MemberRepository memberRepository;

    private final UserRepository userRepository;

    private final CollectorRepository collectorRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUser(UserDTO userDTO) {
        Member member = new Member();
        member.setEmail(userDTO.getEmail());
        member.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        member.setName(userDTO.getName());
        member.setPhone(userDTO.getPhone());
        member.setRole("user");
        memberRepository.save(member);

        Users user = new Users();
        user.setMember(member);
        user.setLocation(userDTO.getLocation());
        user.setAccount(userDTO.getAccount());
        user.setBank(userDTO.getBank());
        userRepository.save(user);
    }

    public void createCollector(CollectorDTO collectorDTO) {
        Member member = new Member();
        member.setEmail(collectorDTO.getEmail());
        member.setPassword(passwordEncoder.encode(collectorDTO.getPassword()));
        member.setName(collectorDTO.getName());
        member.setPhone(collectorDTO.getPhone());
        member.setRole("collector");
        memberRepository.save(member);

        Collectors collector = new Collectors();
        collector.setMember(member);
        collector.setLocation(collectorDTO.getLocation());
        collectorRepository.save(collector);
    }
}
