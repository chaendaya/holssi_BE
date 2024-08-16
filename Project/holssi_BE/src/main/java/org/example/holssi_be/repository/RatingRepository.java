package org.example.holssi_be.repository;

import org.example.holssi_be.entity.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
