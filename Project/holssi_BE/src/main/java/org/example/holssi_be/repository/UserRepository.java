package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String userEmail);
    Users findByPhone(String phone);
}
