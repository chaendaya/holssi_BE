package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByMemberEmail(String email);
}
