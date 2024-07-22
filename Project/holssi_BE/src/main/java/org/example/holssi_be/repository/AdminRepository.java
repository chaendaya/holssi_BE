package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Admins;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admins, Long> {

}